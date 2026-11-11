package kz.zeme.converter.core.ui.compose.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kz.zeme.converter.core.res.R
import kz.zeme.converter.core.res.theme.ConverterTheme
import kz.zeme.converter.core.res.theme.primary

@Composable
fun ModalBottomSheet(
    modifier: Modifier = Modifier,
    showHandle: Boolean = true,
    shape: RoundedCornerShape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp),
    backgroundColor: Color = ConverterTheme.colors.basicDark,
    handleColor: Color = ConverterTheme.colors.textSecondary,
    body: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .clip(shape)
            .background(backgroundColor)
            .then(modifier),
    ) {
        if (showHandle) {
            Icon(
                modifier = Modifier
                    .padding(top = 6.dp, bottom = 10.dp)
                    .fillMaxWidth(),
                painter = painterResource(R.drawable.ic_handle),
                contentDescription = null,
                tint = handleColor,
            )
        }
        body()
    }
}

@Composable
fun ModalBottomSheet(
    modifier: Modifier = Modifier,
    sheetModifier: Modifier = Modifier,
    showHandle: Boolean = true,
    shape: RoundedCornerShape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp),
    backgroundColor: Color = ConverterTheme.colors.basicDark,
    handleColor: Color = ConverterTheme.colors.textSecondary,
    topFloatingView: @Composable ColumnScope.() -> Unit = {},
    body: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier) {
        topFloatingView()

        Column(
            modifier = Modifier
                .clip(shape)
                .background(backgroundColor)
                .then(sheetModifier),
        ) {
            if (showHandle) {
                Icon(
                    modifier = Modifier
                        .padding(top = 6.dp, bottom = 10.dp)
                        .fillMaxWidth(),
                    painter = painterResource(R.drawable.ic_handle),
                    contentDescription = null,
                    tint = handleColor,
                )
            }
            body()
        }
    }
}

@Composable
fun ModalBottomSheetBox(
    modifier: Modifier = Modifier,
    showHandle: Boolean = true,
    shape: RoundedCornerShape = RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp),
    backgroundColor: Color = ConverterTheme.colors.basicDark,
    handleColor: Color = ConverterTheme.colors.textSecondary,
    body: @Composable BoxScope.(PaddingValues) -> Unit,
) {
    Box(
        modifier = Modifier
            .clip(shape)
            .background(backgroundColor)
            .then(modifier),
    ) {
        body(PaddingValues(top = 20.dp))

        if (showHandle) {
            Icon(
                modifier = Modifier
                    .padding(top = 6.dp, bottom = 10.dp)
                    .fillMaxWidth(),
                painter = painterResource(R.drawable.ic_handle),
                contentDescription = null,
                tint = handleColor,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF010101)
@Composable
internal fun ModalBottomSheetPreview() {
    ModalBottomSheet(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "Заголовок шторки",
            style = ConverterTheme.typography.titleMedium.primary(),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF010101)
@Composable
internal fun ModalBottomSheetBoxPreview() {
    ModalBottomSheetBox(modifier = Modifier.fillMaxWidth()) { paddingValues ->
        Text(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
            text = "Слоистая шторка с оверлеем",
            style = ConverterTheme.typography.titleMedium.primary(),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF010101)
@Composable
internal fun ModalBottomSheetFloatingPreview() {
    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        topFloatingView = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(48.dp)
                    .background(Color.DarkGray, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Парящая плашка над шторкой",
                    color = Color.White,
                )
            }
        },
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "Основное тело шторки",
            style = ConverterTheme.typography.titleMedium.primary(),
        )
    }
}
