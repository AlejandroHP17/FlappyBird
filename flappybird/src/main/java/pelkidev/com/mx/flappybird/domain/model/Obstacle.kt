package pelkidev.com.mx.flappybird.domain.model

data class Obstacle(
    val id: Long,
    val x: Float,
    /** Center Y of the gap between top and bottom rocks. */
    val gapCenterY: Float,
    val scored: Boolean = false,
)
