package dev.aleksrychkov.tetromino.presentation.game

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dev.aleksrychkov.tetromino.presentation.game.logic.ButtonController
import dev.aleksrychkov.tetromino.presentation.game.logic.GameDifficulty
import dev.aleksrychkov.tetromino.presentation.game.logic.GameLoop
import dev.aleksrychkov.tetromino.presentation.game.logic.GameState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class GameViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val dataStore: DataStore<Preferences>,
) : ViewModel() {

    private companion object {
        const val KEY_SAVED_STATE = "GameViewModelState"
        const val KEY_UI_STATE = "GameUiState"
        const val KEY_GAME_STATE = "GameGameState"
        val KEY_DIFFICULTY = intPreferencesKey("difficulty")
        val KEY_SCORE_EASY = longPreferencesKey("score_easy")
        val KEY_SCORE_MEDIUM = longPreferencesKey("score_medium")
        val KEY_SCORE_HARD = longPreferencesKey("score_hard")
    }

    private val gameLoop = GameLoop(viewModelScope)
    private val buttonController = ButtonController(viewModelScope, gameLoop)

    private val _state = MutableStateFlow(restoreState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    init {
        observeGameLoop()
        savedStateHandle.setSavedStateProvider(KEY_SAVED_STATE) {
            bundleOf(
                KEY_UI_STATE to _state.value,
                KEY_GAME_STATE to gameLoop.bundle(),
            )
        }
    }

    private fun observeGameLoop() {
        viewModelScope.launch {
            gameLoop.gameState.collect { loopState ->
                val uiFlag = mapToUiFlag(loopState.state)

                if (uiFlag == GameUiState.Flag.GameOver && _state.value.state != GameUiState.Flag.GameOver) {
                    updateHighScore(loopState.difficulty, loopState.score)
                }

                _state.value = _state.value.copy(
                    gameGrid = loopState.gameGrid,
                    score = loopState.score,
                    level = loopState.level,
                    clearedLines = loopState.clearedLines,
                    nextPieceColor = loopState.nextPieceColor,
                    tick = (_state.value.tick + 1).toByte(),
                    state = uiFlag,
                    difficulty = loopState.difficulty
                )
            }
        }
    }

    private fun mapToUiFlag(loopFlag: GameState.Flag) = when (loopFlag) {
        GameState.Flag.GameOver -> GameUiState.Flag.GameOver
        GameState.Flag.Pause, GameState.Flag.Paused -> GameUiState.Flag.Paused
        GameState.Flag.Initial -> GameUiState.Flag.Initial
        else -> GameUiState.Flag.Running
    }

    // --- Lifecycle ---

    fun onResume() {
        if (_state.value.state == GameUiState.Flag.Running) gameLoop.start()
    }

    fun onPause() = gameLoop.stop()

    fun onRestart(difficulty: GameDifficulty) {
        syncDifficulty(difficulty)
        gameLoop.restart()
    }

    fun onStartClicked(difficulty: GameDifficulty) {
        syncDifficulty(difficulty)
        gameLoop.start()
    }

    private fun syncDifficulty(difficulty: GameDifficulty) {
        _state.value = _state.value.copy(
            state = GameUiState.Flag.Running,
            difficulty = difficulty,
            isBestScore = false
        )
        gameLoop.setDifficulty(difficulty)
        viewModelScope.launch { dataStore.edit { it[KEY_DIFFICULTY] = difficulty.ordinal } }
    }

    fun onPauseResumeClicked() {
        if (_state.value.state == GameUiState.Flag.Paused) gameLoop.start() else onPause()
    }

    // --- Controls ---
    fun onUp() = buttonController.onUp()
    fun onDown() = buttonController.onDown()
    fun onDrop() = buttonController.onDrop()
    fun onLeft() = buttonController.onLeft()
    fun onRight() = buttonController.onRight()
    fun onRotateRight() = buttonController.onRotateRight()
    fun onLeftDown() = buttonController.onLeftDown()
    fun onLeftReleased() = buttonController.onLeftReleased()
    fun onRightDown() = buttonController.onRightDown()
    fun onRightRelease() = buttonController.onRightRelease()
    fun onDownDown() = buttonController.onDownDown()
    fun onDownRelease() = buttonController.onDownRelease()

    // --- Data Persistence ---

    private fun restoreState(): GameUiState {
        val bundle = savedStateHandle
            .get<Bundle?>(KEY_SAVED_STATE)
            ?: return GameUiState().also { readDifficulty() }

        bundle.getBundle(KEY_GAME_STATE)?.let { gameLoop.restore(it) }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(KEY_UI_STATE, GameUiState::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(KEY_UI_STATE)
        } ?: GameUiState().also { readDifficulty() }
    }

    private fun readDifficulty() {
        viewModelScope.launch {
            val ordinal = dataStore.data.map { it[KEY_DIFFICULTY] ?: 0 }.first()
            val diff = GameDifficulty.entries[ordinal]
            _state.value = _state.value.copy(difficulty = diff)
            gameLoop.setDifficulty(diff)
        }
    }

    private fun updateHighScore(difficulty: GameDifficulty, score: Long) {
        viewModelScope.launch {
            val key = when (difficulty) {
                GameDifficulty.Easy -> KEY_SCORE_EASY
                GameDifficulty.Medium -> KEY_SCORE_MEDIUM
                GameDifficulty.Hard -> KEY_SCORE_HARD
            }
            val best = dataStore.data.map { it[key] ?: 0L }.first()
            if (score > best) {
                _state.value = _state.value.copy(isBestScore = true)
                dataStore.edit { it[key] = score }
            }
        }
    }
}

object GameViewModelFactory {
    private const val PREFERENCES = "preferences"
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES)
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            val context =
                checkNotNull(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
            val savedStateHandle = createSavedStateHandle()
            GameViewModel(savedStateHandle, context.dataStore)
        }
    }
}
