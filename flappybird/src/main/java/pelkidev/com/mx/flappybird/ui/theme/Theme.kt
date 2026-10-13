package pelkidev.com.mx.flappybird.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val FlappyColorScheme = darkColorScheme(
    primary = AccentOrange,
    secondary = SkyBlue,
    background = SkyBlueDark,
    onPrimary = OnSky,
    onBackground = OnSky,
)

@Composable
fun FlappyBirdTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = FlappyColorScheme,
        typography = Typography,
        content = content,
    )
}
