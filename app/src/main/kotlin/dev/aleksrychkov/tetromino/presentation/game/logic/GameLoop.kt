package dev.aleksrychkov.tetromino.presentation.game.logic

import android.os.Build
import android.os.Bundle
import androidx.core.os.bundleOf
import dev.aleksrychkov.tetromino.presentation.game.logic.piece.Piece
import dev.aleksrychkov.tetromino.presentation.game.logic.piece.RandomBag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import dev.aleksrychkov.tetromino.presentation.game.logic.GameState.Flag as State

internal class GameLoop(
    private val scope: CoroutineScope,
) {
    private companion object {
        const val GRID_FILL_ANIMATION_DELAY = 25L
        const val REMOVE_LINE_ANIMATION_EXTRA_DELAY = 150L
        const val KEY_BUNDLE = "GameLoopStateBundle"
    }

    private val gameContext = Dispatchers.Default.limitedParallelism(1)

    private val gameGrid = GameGrid()
    private val score = GameScore()
    private var level = GameLevel()
    private var delayProvider = GameDelay()
    private val bag = RandomBag()

    private var state: State = State.Initial
    private var currentPiece: Piece? = null
    private var nextPiece: Piece? = null
    private var isGhostEnabled: Boolean = true

    private val _gameState = MutableStateFlow(createGameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private var loopJob: Job? = null

    // --- Public API ---

    fun start() {
        if (loopJob?.isActive == true && state != State.Paused) return

        loopJob?.cancel()
        loopJob = scope.launch(gameContext) {
            if (state == State.Paused) state = State.Tick

            while (isActive) {
                val stepDelay = withGhostManagement { tick() }
                _gameState.value = createGameState()
                if (state == State.Paused || state == State.GameOver) break
                delay(stepDelay)
            }
        }
    }

    fun stop() {
        scope.launch(gameContext) {
            if (state in listOf(State.Tick, State.CheckLines, State.RemoveLines)) {
                state = State.Pause
            }
        }
    }

    fun restart() {
        loopJob?.cancel()
        scope.launch(gameContext) {
            for (y in 0..gameGrid.grid.lastIndex) {
                gameGrid.fillWithColor(y) { 0 }
                _gameState.value = createGameState()
                delay(GRID_FILL_ANIMATION_DELAY)
            }
            state = State.Initial
            start()
        }
    }

    fun setDifficulty(difficulty: GameDifficulty) {
        delayProvider.difficulty = difficulty
    }

    // --- Inputs ---

    fun moveLeft() = dispatch { currentPiece?.left(gameGrid.grid) }
    fun moveRight() = dispatch { currentPiece?.right(gameGrid.grid) }
    fun moveDown() = dispatch { currentPiece?.down(gameGrid.grid) }
    fun rotate() = dispatch { currentPiece?.rotate(gameGrid.grid) }

    fun hardDrop() {
        scope.launch(gameContext) {
            if (state != State.Tick) return@launch
            withGhostManagement {
                while (currentPiece?.down(gameGrid.grid) == true) {
                    /* dropping */
                }
            }
            state = State.CheckLines
            _gameState.value = createGameState()
            loopJob?.cancel()
            start()
        }
    }

    // --- Internal Logic ---

    private fun dispatch(action: () -> Unit) {
        scope.launch(gameContext) {
            if (state != State.Tick) return@launch
            withGhostManagement { action() }
            _gameState.value = createGameState()
        }
    }

    private suspend fun tick(): Long {
        return when (state) {
            State.Initial -> setupNewGame()
            State.Tick -> handleGravity()
            State.Pause -> {
                state = State.Paused
                0L
            }

            State.CheckLines -> handleLineClearing()
            State.RemoveLines -> {
                gameGrid.removeLines()
                state = State.CheckGameOver
                0L
            }

            State.CheckGameOver -> checkGameOver()
            State.GameOver -> 0L
            else -> delayProvider.delay
        }
    }

    private fun setupNewGame(): Long {
        score.reset()
        level.reset()
        delayProvider.reset()
        bag.reset()
        gameGrid.reset()
        currentPiece = bag.next().also { it.start(gameGrid.grid) }
        nextPiece = bag.next()
        state = State.Tick
        return 0L
    }

    private fun handleGravity(): Long {
        val moved = currentPiece?.down(gameGrid.grid) ?: true
        if (!moved) state = State.CheckLines
        return if (moved) delayProvider.delay else 0L
    }

    private fun handleLineClearing(): Long {
        val removedLines = gameGrid.checkLines()
        level.updateLevel(removedLines)
        score.updateScore(removedLines, level.level)
        delayProvider.updateDelay(level.level)

        return if (removedLines > 0) {
            state = State.RemoveLines
            REMOVE_LINE_ANIMATION_DURATION + REMOVE_LINE_ANIMATION_EXTRA_DELAY
        } else {
            state = State.CheckGameOver
            0L
        }
    }

    private suspend fun checkGameOver(): Long {
        if (gameGrid.checkGameOver()) {
            performGameOverAnimation()
            state = State.GameOver
            return 0L
        }
        currentPiece = nextPiece?.apply { start(gameGrid.grid) }
        nextPiece = bag.next()
        state = State.Tick
        return delayProvider.delay
    }

    private suspend fun performGameOverAnimation() {
        for (y in gameGrid.grid.lastIndex downTo 0) {
            gameGrid.fillWithColor(y = y)
            _gameState.value = createGameState()
            delay(GRID_FILL_ANIMATION_DELAY)
        }
    }

    private inline fun <T> withGhostManagement(block: () -> T): T {
        currentPiece?.removeGhost(gameGrid.grid)
        return block().also {
            if (isGhostEnabled) currentPiece?.addGhost(gameGrid.grid)
        }
    }

    // --- State Persistence ---

    fun bundle(): Bundle = bundleOf(KEY_BUNDLE to _gameState.value)

    fun restore(bundle: Bundle) {
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(KEY_BUNDLE, GameState::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(KEY_BUNDLE)
        } ?: return

        scope.launch(gameContext) {
            gameGrid.restore(data.gameGrid.grid)
            score.reset(data.score)
            level.reset(level = data.level, clearedLines = data.clearedLines)
            currentPiece = Piece.from(
                data.currentPieceColor,
                data.currentPieceX,
                data.currentPieceY,
                data.currentPieceTurn
            )
            nextPiece = Piece.from(data.nextPieceColor)
            isGhostEnabled = data.isGhostEnabled
            delayProvider.difficulty = data.difficulty
            state = data.state
            _gameState.value = createGameState()
            if (state != State.GameOver && state != State.Paused) start()
        }
    }

    private fun createGameState() = GameState(
        gameGrid = gameGrid,
        state = state,
        currentPieceColor = currentPiece?.color ?: 0,
        currentPieceTurn = currentPiece?.currentTurn,
        currentPieceX = currentPiece?.currentX,
        currentPieceY = currentPiece?.currentY,
        nextPieceColor = nextPiece?.color ?: 0,
        score = score.score,
        level = level.level,
        clearedLines = level.clearedLines,
        isGhostEnabled = isGhostEnabled,
        difficulty = delayProvider.difficulty,
    )
}
