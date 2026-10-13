package pelkidev.com.mx.flappybird.domain.game

import pelkidev.com.mx.flappybird.domain.model.FlappyConfig
import pelkidev.com.mx.flappybird.domain.model.FlappyState
import pelkidev.com.mx.flappybird.domain.model.GameStatus
import pelkidev.com.mx.flappybird.domain.model.Obstacle
import kotlin.math.min
import kotlin.random.Random

/**
 * Motor puro del Flappy (espejo de la lógica Godot en `godot/scenes`).
 * Sin Android / Compose.
 */
class FlappyEngine(
    private val random: Random = Random.Default,
) {
    fun reset(highScore: Int): FlappyState = FlappyState(highScore = highScore)

    fun tap(state: FlappyState): Pair<FlappyState, FlappyEvent?> {
        return when (state.status) {
            GameStatus.Ready -> {
                val jumped = applyJump(state.copy(status = GameStatus.Playing))
                jumped to FlappyEvent.Started
            }
            GameStatus.Playing -> applyJump(state) to FlappyEvent.Jumped
            GameStatus.GameOver -> state to null
        }
    }

    fun tick(state: FlappyState, dtSec: Float): Pair<FlappyState, List<FlappyEvent>> {
        if (state.status != GameStatus.Playing) {
            // Idle bob + scrolling ground on ready
            if (state.status == GameStatus.Ready) {
                val ground = (state.groundOffset + FlappyConfig.SCROLL_SPEED * dtSec) %
                    FlappyConfig.WORLD_WIDTH
                return state.copy(groundOffset = ground) to emptyList()
            }
            return state to emptyList()
        }

        val events = mutableListOf<FlappyEvent>()
        var next = state

        var vy = next.playerVy + FlappyConfig.GRAVITY * dtSec
        vy = min(vy, FlappyConfig.MAX_FALL_SPEED)
        var y = next.playerY + vy * dtSec
        var rotation = next.playerRotationDeg
        if (vy > 0f && rotation < 90f) {
            rotation = min(90f, rotation + FlappyConfig.ROTATION_SPEED_DEG * dtSec)
        }

        var ground = (next.groundOffset + FlappyConfig.SCROLL_SPEED * dtSec) %
            FlappyConfig.WORLD_WIDTH

        var spawnAcc = next.spawnAccumulator + dtSec
        var obstacles = next.obstacles.map { it.copy(x = it.x - FlappyConfig.SCROLL_SPEED * dtSec) }
        var nextId = next.nextObstacleId
        var score = next.score

        while (spawnAcc >= FlappyConfig.SPAWN_INTERVAL_SEC) {
            spawnAcc -= FlappyConfig.SPAWN_INTERVAL_SEC
            val gapY = random.nextFloat() *
                (FlappyConfig.GAP_MAX_Y - FlappyConfig.GAP_MIN_Y) + FlappyConfig.GAP_MIN_Y
            obstacles = obstacles + Obstacle(
                id = nextId,
                x = FlappyConfig.WORLD_WIDTH + FlappyConfig.SPAWN_X_PADDING,
                gapCenterY = gapY,
            )
            nextId += 1
        }

        // Score when player passes pipe center
        obstacles = obstacles.map { obs ->
            if (!obs.scored && obs.x + FlappyConfig.PIPE_WIDTH / 2f < next.playerX) {
                score += 1
                events += FlappyEvent.Scored
                obs.copy(scored = true)
            } else {
                obs
            }
        }

        obstacles = obstacles.filter { it.x + FlappyConfig.PIPE_WIDTH > -40f }

        next = next.copy(
            playerY = y,
            playerVy = vy,
            playerRotationDeg = rotation,
            groundOffset = ground,
            spawnAccumulator = spawnAcc,
            obstacles = obstacles,
            score = score,
            nextObstacleId = nextId,
            highScore = maxOf(next.highScore, score),
        )

        if (collides(next)) {
            events += FlappyEvent.Crashed
            next = next.copy(status = GameStatus.GameOver, playerVy = 0f)
        }

        return next to events
    }

    private fun applyJump(state: FlappyState): FlappyState =
        state.copy(
            playerVy = -FlappyConfig.JUMP_FORCE,
            playerRotationDeg = -30f,
        )

    private fun collides(state: FlappyState): Boolean {
        val px = state.playerX
        val py = state.playerY
        val r = FlappyConfig.PLAYER_RADIUS

        if (py + r >= FlappyConfig.GROUND_TOP) return true
        if (py - r <= 0f) return true

        val halfGap = FlappyConfig.GAP_HEIGHT / 2f
        for (obs in state.obstacles) {
            val left = obs.x
            val right = obs.x + FlappyConfig.PIPE_WIDTH
            if (px + r < left || px - r > right) continue

            val gapTop = obs.gapCenterY - halfGap
            val gapBottom = obs.gapCenterY + halfGap
            if (py - r < gapTop || py + r > gapBottom) return true
        }
        return false
    }
}

sealed interface FlappyEvent {
    data object Started : FlappyEvent
    data object Jumped : FlappyEvent
    data object Scored : FlappyEvent
    data object Crashed : FlappyEvent
}
