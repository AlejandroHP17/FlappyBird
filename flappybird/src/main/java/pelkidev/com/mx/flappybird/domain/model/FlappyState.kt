package pelkidev.com.mx.flappybird.domain.model

data class FlappyState(
    val status: GameStatus = GameStatus.Ready,
    val playerX: Float = FlappyConfig.PLAYER_START_X,
    val playerY: Float = FlappyConfig.PLAYER_START_Y,
    val playerVy: Float = 0f,
    val playerRotationDeg: Float = 0f,
    val obstacles: List<Obstacle> = emptyList(),
    val score: Int = 0,
    val highScore: Int = 0,
    val groundOffset: Float = 0f,
    val spawnAccumulator: Float = 0f,
    val nextObstacleId: Long = 1L,
)

object FlappyConfig {
    const val WORLD_WIDTH = 480f
    const val WORLD_HEIGHT = 854f

    const val GRAVITY = 1000f
    const val JUMP_FORCE = 320f
    const val MAX_FALL_SPEED = 400f
    const val ROTATION_SPEED_DEG = 120f

    const val SCROLL_SPEED = 150f
    const val SPAWN_INTERVAL_SEC = 2f
    const val SPAWN_X_PADDING = 150f

    const val PLAYER_START_X = 224f
    const val PLAYER_START_Y = 363f
    const val PLAYER_RADIUS = 28f

    const val PIPE_WIDTH = 88f
    const val GAP_HEIGHT = 210f
    const val GAP_MIN_Y = 260f
    const val GAP_MAX_Y = 560f

    const val GROUND_TOP = 818f
}
