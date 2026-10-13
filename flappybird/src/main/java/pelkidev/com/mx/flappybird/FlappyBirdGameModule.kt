package pelkidev.com.mx.flappybird

import androidx.compose.runtime.Composable
import pelkidev.com.mx.flappybird.presentation.FlappyScreen
import pelkidev.com.mx.flappybird.ui.theme.FlappyBirdTheme
import pelkidev.com.mx.minijuegos.sdk.GameModule

object FlappyBirdGameModule : GameModule {
    override val id: String = "flappybird"
    override val title: String = "Flappy Bird"
    override val description: String = "Esquiva las rocas y supera tu récord"

    @Composable
    override fun Entry(onExit: () -> Unit) {
        FlappyBirdTheme {
            FlappyScreen(onExit = onExit)
        }
    }
}
