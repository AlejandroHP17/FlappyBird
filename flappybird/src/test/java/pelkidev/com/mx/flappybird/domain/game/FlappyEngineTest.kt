package pelkidev.com.mx.flappybird.domain.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import pelkidev.com.mx.flappybird.domain.model.FlappyConfig
import pelkidev.com.mx.flappybird.domain.model.GameStatus
import pelkidev.com.mx.flappybird.domain.model.Obstacle
import kotlin.random.Random

class FlappyEngineTest {
    private val engine = FlappyEngine(Random(0))

    @Test
    fun tapStartsGameAndJumps() {
        val ready = engine.reset(highScore = 3)
        val (playing, event) = engine.tap(ready)
        assertEquals(GameStatus.Playing, playing.status)
        assertEquals(FlappyEvent.Started, event)
        assertEquals(-FlappyConfig.JUMP_FORCE, playing.playerVy)
        assertEquals(3, playing.highScore)
    }

    @Test
    fun fallingHitsGround() {
        var state = engine.reset(0)
        state = engine.tap(state).first
        state = state.copy(playerY = FlappyConfig.GROUND_TOP - 5f, playerVy = 400f)
        val (after, events) = engine.tick(state, 0.05f)
        assertEquals(GameStatus.GameOver, after.status)
        assertTrue(events.contains(FlappyEvent.Crashed))
    }

    @Test
    fun scoringWhenPipeCenterPassesPlayer() {
        var state = engine.reset(0)
        state = engine.tap(state).first
        // Pipe still to the right of player; after scroll its center crosses playerX
        val pipeX = state.playerX - FlappyConfig.PIPE_WIDTH / 2f + 5f
        state = state.copy(
            playerY = 400f,
            playerVy = 0f,
            obstacles = listOf(Obstacle(id = 1, x = pipeX, gapCenterY = 400f)),
            spawnAccumulator = 0f,
        )
        val (after, events) = engine.tick(state, 0.1f)
        assertTrue(
            "expected score event or scored flag, got score=${after.score} events=$events",
            after.score >= 1 || events.contains(FlappyEvent.Scored),
        )
    }
}
