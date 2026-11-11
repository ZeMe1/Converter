package kz.zeme.converter.core.res.theme

import androidx.compose.runtime.Composable

object ConverterTheme {

    val colors: ConverterColors
        @Composable
        get() = LocalStaticColors.current

    val typography: ConverterTypography
        @Composable
        get() = LocalStaticTypography.current
}
