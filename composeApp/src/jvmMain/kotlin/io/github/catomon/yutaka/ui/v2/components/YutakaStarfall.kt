package io.github.catomon.yutaka.ui.v2.components

import androidx.compose.animation.VectorConverter
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import io.github.catomon.yutaka.ui.v2.theme.YutakaTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.imageResource
import yutaka.composeapp.generated.resources.Res
import yutaka.composeapp.generated.resources.star_sparkle
import kotlin.math.roundToInt
import kotlin.random.Random

private data class YutakaStar(
    var x: Float = Random.nextFloat(),
    var scale: Float = 0.6f + Random.nextFloat() * 0.8f,
    var y: Animatable<Float, AnimationVector1D> = Animatable(-0.2f - Random.nextFloat()),
    var rotation: Animatable<Float, AnimationVector1D> = Animatable(Random.nextFloat() * 360f),
    var color: Animatable<Color, AnimationVector4D> = Animatable(
        Color(0xFFF6A6C1),
        Color.VectorConverter(Color(0xFFF6A6C1).colorSpace),
    ),
)

/**
 * Slow falling, slowly rotating stars in Yutaka's pink, green and amber.
 */
@Composable
fun YutakaStarfall(
    modifier: Modifier = Modifier,
    starCount: Int = 40,
    starSize: Dp = 28.dp,
    fallDuration: Int = 20000,
) {
    val image = imageResource(Res.drawable.star_sparkle)
    val palette = YutakaTheme.colors.starColors
    val density = LocalDensity.current
    val baseSizePx = with(density) { starSize.toPx() }

    val stars = remember {
        List(starCount) {
            YutakaStar(color = Animatable(palette.random(), Color.VectorConverter(palette.first().colorSpace)))
        }
    }

    LaunchedEffect(palette) {
        stars.forEach { star ->
            launch {
                while (true) {
                    star.y.snapTo(-0.2f - Random.nextFloat())
                    star.y.animateTo(
                        targetValue = 1.15f,
                        animationSpec = tween(
                            durationMillis = fallDuration + (Random.nextFloat() * fallDuration * 0.6f).toInt(),
                            easing = LinearEasing,
                        ),
                    )
                    delay(Random.nextLong(300, 3500))
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        stars.forEach { star ->
            launch {
                while (true) {
                    star.rotation.animateTo(
                        targetValue = star.rotation.value + 360f,
                        animationSpec = tween(
                            durationMillis = 4000 + Random.nextInt(4000),
                            easing = LinearEasing,
                        ),
                    )
                    star.rotation.snapTo(0f)
                }
            }
        }
    }

    LaunchedEffect(palette) {
        stars.forEach { star ->
            launch {
                while (true) {
                    delay(Random.nextLong(1800, 5500))
                    star.color.animateTo(palette.random(), tween(1500))
                }
            }
        }
    }

    Canvas(modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        stars.forEach { star ->
            val starPx = baseSizePx * star.scale
            val xPos = star.x * (canvasWidth - starPx)
            val yPos = star.y.value * (canvasHeight - starPx)

            rotate(
                degrees = star.rotation.value,
                pivot = Offset(xPos + starPx / 2f, yPos + starPx / 2f),
            ) {
                drawImage(
                    image = image,
                    dstOffset = IntOffset(xPos.roundToInt(), yPos.roundToInt()),
                    dstSize = IntSize(starPx.roundToInt(), starPx.roundToInt()),
                    colorFilter = ColorFilter.tint(star.color.value),
                )
            }
        }
    }
}
