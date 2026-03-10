package dev.aleksrychkov.tetromino.presentation.game.logic

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

internal class ButtonController(
    private val scope: CoroutineScope,
    private val gameLoop: GameLoop,
) {
    private companion object {
        const val DAS_STARTUP = 266L    // 16 frames
        const val DAS_ITERATION = 100L  // 6 frames
        const val SOFT_DROP_SPEED = 33L // ~2 frames
    }

    private var moveJob: Job? = null

    fun onLeftDown() = startMovement(DAS_STARTUP, DAS_ITERATION) { gameLoop.moveLeft() }
    fun onRightDown() = startMovement(DAS_STARTUP, DAS_ITERATION) { gameLoop.moveRight() }
    fun onDownDown() = startMovement(0L, SOFT_DROP_SPEED) { gameLoop.moveDown() }

    fun onLeftReleased() = cancelJob()
    fun onRightRelease() = cancelJob()
    fun onDownRelease() = cancelJob()

    fun onUp() = gameLoop.rotate()
    fun onRotateRight() = gameLoop.rotate()
    fun onDrop() = gameLoop.hardDrop()

    fun onLeft() = gameLoop.moveLeft()
    fun onRight() = gameLoop.moveRight()
    fun onDown() = gameLoop.moveDown()

    private fun startMovement(startupDelay: Long, iterationDelay: Long, action: () -> Unit) {
        cancelJob()
        moveJob = scope.launch(Dispatchers.Default) {
            action() // First move is always instant
            if (startupDelay > 0) delay(startupDelay)

            while (isActive) {
                action()
                delay(iterationDelay)
            }
        }
    }

    private fun cancelJob() {
        moveJob?.cancel()
        moveJob = null
    }
}