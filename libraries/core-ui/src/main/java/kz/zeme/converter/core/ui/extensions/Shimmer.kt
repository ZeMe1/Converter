package kz.zeme.converter.core.ui.extensions

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kz.zeme.converter.core.res.theme.ConverterTheme

@Composable
fun TextStyle.shimmer(
    enabled: Boolean,
    backgroundColor: Color = this.color,
    shimmerColor: Color = ConverterTheme.colors.mainShimmer,
): TextStyle = copy(
    brush = if (enabled) shimmerBrush(
        startOffset = Offset(0f, 150f),
        backgroundColor = backgroundColor,
        shimmerColor = shimmerColor,
    ) else SolidColor(backgroundColor),
)

@Composable
fun shimmerBrush(
    backgroundColor: Color = ConverterTheme.colors.mainShimmer,
    shimmerColor: Color = remember { Color(color = 0xFF272727) },
    durationMs: Int = 800,
    startOffset: Offset = Offset(0f, 0f),
): Brush = shimmerBrush(
    startOffset = startOffset,
    gradientColors = listOf(
        backgroundColor,
        shimmerColor,
        backgroundColor,
        backgroundColor,
    ),
    durationMs = durationMs,
)

@Composable
fun shimmerBrush(
    gradientColors: List<Color>,
    durationMs: Int = 800,
    startOffset: Offset = Offset(0f, 0f),
): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")

    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMs,
                easing = LinearEasing,
            ),
        ),
        label = "shimmerTranslate",
    )

    return linearGradient(
        colors = gradientColors,
        start = startOffset,
        end = Offset(
            x = translateAnimation.value,
            y = translateAnimation.value,
        ),
    )
}

@Composable
@Preview(showBackground = true, name = "Shimmer")
private fun ShimmerPreview() {
    Spacer(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(
                brush = shimmerBrush(
                    startOffset = Offset(50f, 50f),
                    backgroundColor = Color.DarkGray,
                    shimmerColor = Color.Transparent,
                ),
            ),
    )
}
