package kz.zeme.converter.core.ui.compose.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.contentPaddingWithLabel
import androidx.compose.material3.TextFieldDefaults.contentPaddingWithoutLabel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kz.zeme.converter.core.res.R
import kz.zeme.converter.core.res.theme.ContentAlpha
import kz.zeme.converter.core.res.theme.ConverterTheme
import kz.zeme.converter.core.res.theme.enabled
import kz.zeme.converter.core.res.theme.error
import kz.zeme.converter.core.res.theme.primary
import kz.zeme.converter.core.res.theme.secondary

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
@Composable
fun TextField(
    modifier: Modifier = Modifier,
    minHeight: Dp = 56.dp,
    text: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    allowOverflow: Boolean = true,
    textStyle: TextStyle = ConverterTheme.typography.bodyLarge.primary().enabled(enabled),
    hint: String = "",
    hintStyle: TextStyle = ConverterTheme.typography.bodyLarge.secondary().enabled(enabled),
    label: String? = null,
    labelStyle: TextStyle = ConverterTheme.typography.labelSmall.secondary().enabled(enabled),
    error: String? = null,
    errorStyle: TextStyle = ConverterTheme.typography.bodySmall.error().enabled(enabled),
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    maxChar: Int = Int.MAX_VALUE,
    cursorHandleColor: Color = Color.Transparent,
    textDisabledColor: Color = textStyle.color.copy(alpha = ContentAlpha.disabled),
    iconStart: @Composable (() -> Unit)? = null,
    iconEnd: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    requestFocus: Boolean = false,
    focusedBorderColor: Color = ConverterTheme.colors.basicGray,
    errorBorderColor: Color = ConverterTheme.colors.basicError,
    containerColor: Color = ConverterTheme.colors.buttonSecondary,
    errorContainerColor: Color = ConverterTheme.colors.inputErrorBg,
    contentPadding: PaddingValues? = null,
    shape: Shape = RoundedCornerShape(8.dp),
    filter: ((text: String) -> String)? = null,
    onValueChange: ((text: String) -> Unit)? = null,
) {
    val errorText =
        if (text.length >= maxChar) stringResource(id = R.string.shared_max_char, maxChar)
        else error.orEmpty()

    val hasError = errorText.isNotEmpty()

    val textState = remember {
        val initialText =
            if (allowOverflow) text
            else text.take(maxChar)

        mutableStateOf(
            TextFieldValue(
                text = initialText,
                selection = TextRange(initialText.length),
            ),
        )
    }

    val customTextSelectionColors = TextSelectionColors(
        handleColor = cursorHandleColor,
        backgroundColor = Color.Gray,
    )
    val interactionSource = remember { MutableInteractionSource() }
    val hasFocus = interactionSource.collectIsFocusedAsState()
    val focusRequester = remember { FocusRequester() }

    val borderColor =
        if (!enabled) Color.Transparent
        else if (readOnly) Color.Transparent
        else if (hasError) errorBorderColor
        else if (hasFocus.value) focusedBorderColor
        else Color.Transparent

    val backgroundColor =
        if (!enabled) ConverterTheme.colors.buttonDisabled
        else if (hasError) errorContainerColor
        else containerColor

    val cursorColor =
        if (hasError) ConverterTheme.colors.basicError
        else ConverterTheme.colors.basicGray

    LaunchedEffect(text) {
        if (text != textState.value.text) {
            textState.value = textState.value.copy(
                text = text,
                selection = TextRange(text.length),
            )
        }
    }

    Column(
        modifier = modifier,
    ) {
        CompositionLocalProvider(
            LocalTextSelectionColors provides customTextSelectionColors,
        ) {
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight)
                    .focusRequester(focusRequester),
                value = textState.value,
                singleLine = maxLines == 1,
                maxLines = maxLines,
                minLines = minLines,
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                textStyle = textStyle.copy(
                    color = if (enabled) textStyle.color else textDisabledColor,
                ),
                onValueChange = { update: TextFieldValue ->
                    val newText =
                        if (allowOverflow) update.text
                        else update.text.take(maxChar)

                    val filtered = filter?.invoke(newText)
                    textState.value = update.copy(text = filtered ?: newText)
                    onValueChange?.invoke(textState.value.text)
                },
                readOnly = readOnly,
                enabled = enabled && !readOnly,
                interactionSource = interactionSource,
                cursorBrush = SolidColor(cursorColor),
                decorationBox = { innerTextField ->
                    TextFieldDefaults.DecorationBox(
                        placeholder = {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = hint,
                                style = hintStyle,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                        label = if (label != null && textState.value.text.isNotEmpty()) {
                            @Composable {
                                Text(
                                    text = label,
                                    style = labelStyle,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        } else null,
                        leadingIcon = iconStart,
                        trailingIcon = iconEnd,
                        visualTransformation = visualTransformation,
                        value = textState.value.text,
                        innerTextField = innerTextField,
                        enabled = enabled && !readOnly,
                        singleLine = maxLines == 1,
                        interactionSource = interactionSource,
                        isError = hasError,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            cursorColor = Color.Transparent,
                        ),
                        contentPadding =
                            contentPadding
                                ?: if (minHeight < 56.dp) PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                                else if (label == null) contentPaddingWithoutLabel()
                                else contentPaddingWithLabel(),
                        container = {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = backgroundColor,
                                        shape = shape,
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = borderColor,
                                        shape = shape,
                                    ),
                            )
                        },
                    )
                },
            )
        }

        if (enabled && errorText.isNotEmpty()) {
            Text(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth(),
                text = errorText,
                style = errorStyle,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }

    if (enabled && requestFocus) {
        LaunchedEffect(focusRequester) {
            focusRequester.requestFocus()
        }
    }
}

internal data class TextFieldPreviewState(
    val text: String,
    val hint: String = "",
    val label: String? = null,
    val error: String? = null,
    val enabled: Boolean = true,
    val hasStartIcon: Boolean = false,
    val hasEndIcon: Boolean = false,
    val allowOverflow: Boolean = true,
    val maxLines: Int = Int.MAX_VALUE,
    val minHeight: Dp = 56.dp,
    val maxChar: Int = Int.MAX_VALUE,
)

internal class TextFieldPreviewParameterProvider : PreviewParameterProvider<TextFieldPreviewState> {
    override val values = sequenceOf(
        TextFieldPreviewState(
            text = "",
            hint = "Введите текст",
            enabled = true,
            hasStartIcon = true,
        ),
        TextFieldPreviewState(
            text = "",
            hint = "Заблокированное поле",
            enabled = false,
            hasStartIcon = true,
        ),
        TextFieldPreviewState(
            text = "Заполненный текст",
            enabled = true,
            hasStartIcon = true,
        ),
        TextFieldPreviewState(
            text = "Поисковый запрос",
            hint = "Поиск",
            label = "Заголовок поля",
            maxLines = 1,
            minHeight = 48.dp,
            hasStartIcon = false,
        ),
        TextFieldPreviewState(
            text = "Некорректное значение",
            hint = "Email",
            error = "Неверный формат значения",
            hasEndIcon = true,
        ),
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF010101,
    name = "StandardTextField",
)
@Composable
internal fun TextFieldPreview(
    @PreviewParameter(TextFieldPreviewParameterProvider::class) state: TextFieldPreviewState,
) {
    TextField(
        modifier = Modifier.padding(16.dp),
        text = state.text,
        hint = state.hint,
        label = state.label,
        error = state.error,
        maxChar = state.maxChar,
        maxLines = state.maxLines,
        minHeight = state.minHeight,
        allowOverflow = state.allowOverflow,
        enabled = state.enabled,
        iconStart = if (state.hasStartIcon) {
            {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(24.dp)
                        .background(ConverterTheme.colors.basicGray, CircleShape),
                )
            }
        } else null,
        iconEnd = if (state.hasEndIcon) {
            {
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(24.dp)
                        .background(ConverterTheme.colors.basicError, CircleShape),
                )
            }
        } else null,
    )
}
