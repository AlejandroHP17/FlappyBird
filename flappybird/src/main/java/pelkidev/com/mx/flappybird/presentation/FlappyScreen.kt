package pelkidev.com.mx.flappybird.presentation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import pelkidev.com.mx.flappybird.R
import pelkidev.com.mx.flappybird.domain.model.FlappyConfig
import pelkidev.com.mx.flappybird.domain.model.FlappyState
import kotlin.math.ceil
import kotlin.math.roundToInt

@Composable
fun FlappyScreen(
    onExit: () -> Unit,
    viewModel: FlappyViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        FlappyGameCanvas(
            state = state,
            onTap = viewModel::onTap,
            modifier = Modifier.fillMaxSize(),
        )

        when {
            state.isReady -> ReadyOverlay()
            state.isGameOver -> GameOverOverlay(
                score = state.score,
                highScore = state.highScore,
                onRestart = viewModel::restart,
            )
        }

        if (!state.isGameOver) {
            Text(
                text = state.score.toString(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(top = 16.dp),
                color = Color.White,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        IconButton(
            onClick = onExit,
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(4.dp),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.flappybird_back),
                tint = Color.White,
            )
        }
    }
}

@Composable
private fun ReadyOverlay() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 120.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.flappybird_text_get_ready),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(horizontal = 24.dp),
            contentScale = ContentScale.FillWidth,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Image(
            painter = painterResource(R.drawable.flappybird_tap),
            contentDescription = stringResource(R.string.flappybird_tap_hint),
            modifier = Modifier.size(96.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.flappybird_tap_to_start),
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
private fun GameOverOverlay(
    score: Int,
    highScore: Int,
    onRestart: () -> Unit,
) {
    val medal = remember(score) {
        when {
            score >= 4 -> R.drawable.flappybird_medal_gold
            score >= 2 -> R.drawable.flappybird_medal_silver
            else -> R.drawable.flappybird_medal_bronze
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.35f))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.flappybird_text_game_over),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .padding(bottom = 16.dp),
            contentScale = ContentScale.FillWidth,
        )
        Box(
            modifier = Modifier.widthIn(max = 280.dp),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.flappybird_ui_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
            )
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(medal),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.flappybird_score_label, score),
                    color = Color(0xFF3D2C1E),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )
                Text(
                    text = stringResource(R.string.flappybird_best_label, highScore),
                    color = Color(0xFF3D2C1E),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onRestart) {
            Text(
                text = stringResource(R.string.flappybird_play_again),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
        }
    }
}

@Composable
private fun FlappyGameCanvas(
    state: FlappyState,
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val background = ImageBitmap.imageResource(R.drawable.flappybird_background)
    val ground = ImageBitmap.imageResource(R.drawable.flappybird_ground)
    val rock = ImageBitmap.imageResource(R.drawable.flappybird_rock)
    val rockDown = ImageBitmap.imageResource(R.drawable.flappybird_rock_down)
    val planes = listOf(
        ImageBitmap.imageResource(R.drawable.flappybird_plane_1),
        ImageBitmap.imageResource(R.drawable.flappybird_plane_2),
        ImageBitmap.imageResource(R.drawable.flappybird_plane_3),
    )

    val flap = rememberInfiniteTransition(label = "flap")
    val flapFrame by flap.animateFloat(
        initialValue = 0f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(240, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "flapFrame",
    )
    val plane = planes[flapFrame.toInt().coerceIn(0, 2)]

    Canvas(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures { onTap() }
        },
    ) {
        val scale = minOf(
            size.width / FlappyConfig.WORLD_WIDTH,
            size.height / FlappyConfig.WORLD_HEIGHT,
        )
        val offsetX = (size.width - FlappyConfig.WORLD_WIDTH * scale) / 2f
        val offsetY = (size.height - FlappyConfig.WORLD_HEIGHT * scale) / 2f

        drawRect(Color(0xFF4EC0CA))

        withTransform({
            translate(left = offsetX, top = offsetY)
            scale(scaleX = scale, scaleY = scale, pivot = Offset.Zero)
        }) {
            drawScaledImage(
                image = background,
                dstOffset = Offset(0f, -45f),
                dstSize = Size(FlappyConfig.WORLD_WIDTH, FlappyConfig.WORLD_HEIGHT + 90f),
            )

            val halfGap = FlappyConfig.GAP_HEIGHT / 2f
            state.obstacles.forEach { obs ->
                val pipeW = FlappyConfig.PIPE_WIDTH
                val topH = (obs.gapCenterY - halfGap).coerceAtLeast(40f)
                val bottomTop = obs.gapCenterY + halfGap
                val bottomH = (FlappyConfig.GROUND_TOP - bottomTop).coerceAtLeast(40f)

                drawScaledImage(
                    image = rockDown,
                    dstOffset = Offset(obs.x, obs.gapCenterY - halfGap - topH),
                    dstSize = Size(pipeW, topH),
                )
                drawScaledImage(
                    image = rock,
                    dstOffset = Offset(obs.x, bottomTop),
                    dstSize = Size(pipeW, bottomH),
                )
            }

            val groundY = FlappyConfig.GROUND_TOP
            val groundH = FlappyConfig.WORLD_HEIGHT - groundY + 40f
            val tileW = FlappyConfig.WORLD_WIDTH
            val tiles = ceil(2.0).toInt() + 1
            val baseX = -(state.groundOffset % tileW)
            repeat(tiles) { i ->
                drawScaledImage(
                    image = ground,
                    dstOffset = Offset(baseX + i * tileW, groundY),
                    dstSize = Size(tileW, groundH),
                )
            }

            withTransform({
                translate(left = state.playerX, top = state.playerY)
                rotate(degrees = state.playerRotationDeg, pivot = Offset.Zero)
            }) {
                val pw = FlappyConfig.PLAYER_RADIUS * 2.4f
                val ph = FlappyConfig.PLAYER_RADIUS * 1.8f
                drawScaledImage(
                    image = plane,
                    dstOffset = Offset(-pw / 2f, -ph / 2f),
                    dstSize = Size(pw, ph),
                )
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawScaledImage(
    image: ImageBitmap,
    dstOffset: Offset,
    dstSize: Size,
) {
    drawImage(
        image = image,
        srcOffset = IntOffset.Zero,
        srcSize = IntSize(image.width, image.height),
        dstOffset = IntOffset(dstOffset.x.roundToInt(), dstOffset.y.roundToInt()),
        dstSize = IntSize(dstSize.width.roundToInt(), dstSize.height.roundToInt()),
    )
}
