package kz.zeme.converter.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kz.zeme.converter.R
import kz.zeme.converter.core.res.R as CoreResR
import kz.zeme.converter.core.res.theme.ConverterTheme

private const val SPLASH_DURATION_MS = 1500L

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
) {
    LaunchedEffect(Unit) {
        delay(SPLASH_DURATION_MS)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ConverterTheme.colors.mainBg)
            .systemBarsPadding(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(ConverterTheme.colors.basicDark),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(CoreResR.drawable.ic_splash_logo),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.app_name),
                style = ConverterTheme.typography.titleLarge,
                color = ConverterTheme.colors.textPrimary,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    SplashScreen(
        onSplashFinished = {},
    )
}
