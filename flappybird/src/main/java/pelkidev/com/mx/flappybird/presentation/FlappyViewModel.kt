package pelkidev.com.mx.flappybird.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import pelkidev.com.mx.flappybird.data.HighScoreRepository
import pelkidev.com.mx.flappybird.domain.game.FlappyEngine
import pelkidev.com.mx.flappybird.domain.game.FlappyEvent
import pelkidev.com.mx.flappybird.domain.model.FlappyState
import pelkidev.com.mx.flappybird.domain.model.GameStatus

class FlappyViewModel(application: Application) : AndroidViewModel(application) {
    private val engine = FlappyEngine()
    private val scores = HighScoreRepository(application)
    private val sounds = FlappySounds(application)

    private val _state = MutableStateFlow(engine.reset(scores.load()))
    val state: StateFlow<FlappyState> = _state.asStateFlow()

    private var loopJob: Job? = null

    init {
        startLoop()
    }

    fun onTap() {
        val (next, event) = engine.tap(_state.value)
        _state.value = next
        when (event) {
            FlappyEvent.Started, FlappyEvent.Jumped -> sounds.playWing()
            else -> Unit
        }
    }

    fun restart() {
        scores.save(_state.value.highScore)
        _state.value = engine.reset(scores.load())
    }

    private fun startLoop() {
        loopJob?.cancel()
        loopJob = viewModelScope.launch {
            var last = System.nanoTime()
            while (isActive) {
                delay(16L)
                val now = System.nanoTime()
                val dt = ((now - last) / 1_000_000_000.0).toFloat().coerceIn(0f, 0.05f)
                last = now
                val (next, events) = engine.tick(_state.value, dt)
                if (events.isNotEmpty() || next != _state.value) {
                    _state.value = next
                    events.forEach { handle(it, next) }
                }
            }
        }
    }

    private fun handle(event: FlappyEvent, state: FlappyState) {
        when (event) {
            FlappyEvent.Scored -> {
                sounds.playPoint()
                scores.save(state.score)
            }
            FlappyEvent.Crashed -> {
                sounds.playHit()
                sounds.playDie()
                scores.save(state.highScore)
            }
            else -> Unit
        }
    }

    override fun onCleared() {
        loopJob?.cancel()
        sounds.release()
        super.onCleared()
    }
}

val FlappyState.isPlaying: Boolean get() = status == GameStatus.Playing
val FlappyState.isGameOver: Boolean get() = status == GameStatus.GameOver
val FlappyState.isReady: Boolean get() = status == GameStatus.Ready
