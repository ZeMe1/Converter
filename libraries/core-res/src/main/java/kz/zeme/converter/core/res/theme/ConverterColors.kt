package kz.zeme.converter.core.res.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class ConverterColors(
    val basicDark: Color,
    val basicGray: Color,
    val basicError: Color,
    val basicSuccess: Color,
    val inputBg: Color,
    val inputErrorBg: Color,
    val mainBg: Color,
    val mainShimmer: Color,
    val buttonPrimary: Color,
    val buttonSecondary: Color,
    val buttonDisabled: Color,
    val buttonDarkGrayed: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val gray8: Color,
)

val LocalStaticColors = staticCompositionLocalOf {
    ConverterColors(
        basicDark = Basic_Dark,
        basicGray = Basic_Gray,
        basicError = Basic_Error,
        basicSuccess = Basic_Success,
        inputBg = Input_Bg,
        inputErrorBg = Input_Error_Bg,
        mainBg = Main_Bg,
        mainShimmer = Main_Shimmer,
        buttonPrimary = Button_Primary,
        buttonSecondary = Button_Secondary,
        buttonDarkGrayed = Button_Dark_Grayed,
        buttonDisabled = Button_Disabled,
        textPrimary = Text_Primary,
        textSecondary = Text_Secondary,
        gray8 = Gray_8,
    )
}

@Composable
fun Color.enabled(enabled: Boolean): Color = copy(
    alpha = ContentAlpha.getAlpha(enabled),
)

@Suppress("ConstPropertyName")
object ContentAlpha {
    const val disabled: Float = 0.38f

    fun getAlpha(
        enabled: Boolean,
        disabled: Float = ContentAlpha.disabled,
    ): Float = if (enabled) 1f else disabled
}

internal val Basic_Gray = Color(color = 0xFF898989)
internal val Basic_Dark = Color(color = 0xFF16171B)
internal val Input_Bg = Color(color = 0xFF2D2D30)
internal val Input_Error_Bg = Color(color = 0xFF3F2B2F)
internal val Main_Bg = Color(color = 0xFF010101)
internal val Basic_Error = Color(color = 0xFFDC1B27)
internal val Basic_Success = Color(color = 0xFF00D394)
internal val Button_Primary = Color(color = 0xFF2C2C2C)
internal val Button_Secondary = Color(color = 0xFF2B2B2E)
internal val Button_Disabled = Color(color = 0xFF202024)
internal val Button_Dark_Grayed = Color(0xE58B8787)
internal val Main_Shimmer = Color(color = 0xFF121316)
internal val Text_Primary = Color.White
internal val Text_Secondary = Basic_Gray
internal val Gray_8 = Color(color = 0xFF48494A)
