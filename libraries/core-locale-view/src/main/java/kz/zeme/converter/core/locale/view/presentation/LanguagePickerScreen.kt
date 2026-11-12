package kz.zeme.converter.core.locale.view.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import kz.zeme.converter.core.error.BaseError
import kz.zeme.converter.core.error.UnexpectedError
import kz.zeme.converter.core.locale.domain.Language
import kz.zeme.converter.core.locale.domain.Language.LanguageCode.En
import kz.zeme.converter.core.locale.domain.Language.LanguageCode.Kz
import kz.zeme.converter.core.locale.domain.Language.LanguageCode.Ru
import kz.zeme.converter.core.observable.ViewState
import kz.zeme.converter.core.res.R
import kz.zeme.converter.core.res.theme.ConverterTheme
import kz.zeme.converter.core.res.theme.primary
import kz.zeme.converter.core.ui.components.DefaultErrorScreen
import kz.zeme.converter.core.ui.compose.views.ModalBottomSheet
import kz.zeme.converter.core.ui.extensions.compose.clickableHaptic
import kz.zeme.converter.core.ui.extensions.shimmer

@Composable
fun LanguagePickerScreen(
    state: State<ViewState<List<Language>>?>,
    controller: LanguagePickerController,
) {
    LanguagePickerScreen(
        viewState = state.value,
        controller = controller,
    )
}

@Composable
private fun LanguagePickerScreen(
    viewState: ViewState<List<Language>>?,
    controller: LanguagePickerController,
) {
    ModalBottomSheet(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = stringResource(R.string.language_picker_title),
            style = ConverterTheme.typography.titleMedium.primary(),
        )

        when (viewState) {
            null,
            is ViewState.Loading.Skeleton -> LanguagePickerShimmerView()

            is ViewState.Error -> DefaultErrorView(
                error = viewState.error,
                onRefresh = controller::onStart,
            )

            is ViewState.Loading.ProgressBar,
            is ViewState.Data -> viewState.data?.let {
                LanguageListContent(
                    languages = it,
                    controller = controller,
                )
            } ?: LanguagePickerShimmerView()
        }
    }
}

@Composable
private fun DefaultErrorView(
    modifier: Modifier = Modifier,
    error: BaseError? = null,
    onRefresh: () -> Unit,
) {
    DefaultErrorScreen(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp),
        errorMessage = error?.message ?: stringResource(R.string.shared_something_went_wrong),
        onRefresh = onRefresh,
        backgroundColor = ConverterTheme.colors.basicDark,
    )
}

@Composable
private fun LanguagePickerShimmerView(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        repeat(3) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(ConverterTheme.colors.mainShimmer)
                        .shimmer(),
                )
                Spacer(modifier = Modifier.width(12.dp))
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(ConverterTheme.colors.mainShimmer)
                        .shimmer(),
                )
            }
        }
    }
}

@Composable
private fun LanguageListContent(
    languages: List<Language>,
    controller: LanguagePickerController,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
    ) {
        items(items = languages) { language ->
            LanguageItemRow(
                language = language,
                onClick = { controller.onLanguageClick(language) },
            )
            if (languages.lastOrNull() != language) {
                HorizontalDivider(
                    modifier = Modifier.padding(start = 16.dp),
                    color = ConverterTheme.colors.buttonDisabled.copy(alpha = 0.5f),
                    thickness = 1.dp,
                )
            }
        }
    }
}

@Composable
private fun LanguageItemRow(
    language: Language,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickableHaptic(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier
                .padding(end = 12.dp)
                .size(24.dp)
                .alpha(if (language.isSelected) 1f else 0f),
            painter = painterResource(R.drawable.ic_check_mark),
            contentDescription = null,
            tint = ConverterTheme.colors.textPrimary,
        )

        val titleRes = when (language.code) {
            Ru -> R.string.language_picker_language_ru
            Kz -> R.string.language_picker_language_kz
            En -> R.string.language_picker_language_en
        }

        Text(
            text = stringResource(titleRes),
            style = ConverterTheme.typography.bodyLarge.primary(),
            textAlign = TextAlign.Start,
        )
    }
}

private val languagePickerControllerStub = object : LanguagePickerController {
    override fun onLanguageClick(language: Language) = Unit
    override fun onClose() = Unit
    override fun onStart() = Unit
}

private val languageListStub = listOf(
    Language(isSelected = true, code = Ru),
    Language(isSelected = false, code = Kz),
    Language(isSelected = false, code = En),
)

private data class LanguagePickerScreenPreviewParameter(
    val state: ViewState<List<Language>>,
)

private class LanguagePickerScreenPreviewParameterProvider :
    PreviewParameterProvider<LanguagePickerScreenPreviewParameter> {

    override val values: Sequence<LanguagePickerScreenPreviewParameter> = sequenceOf(
        LanguagePickerScreenPreviewParameter(
            state = ViewState.Data(languageListStub),
        ),
        LanguagePickerScreenPreviewParameter(
            state = ViewState.Data(emptyList()),
        ),
        LanguagePickerScreenPreviewParameter(
            state = ViewState.Loading.Skeleton(),
        ),
        LanguagePickerScreenPreviewParameter(
            state = ViewState.Error(UnexpectedError()),
        ),
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF010101,
)
@Composable
private fun LanguagePickerScreenPreview(
    @PreviewParameter(LanguagePickerScreenPreviewParameterProvider::class)
    params: LanguagePickerScreenPreviewParameter,
) {
    LanguagePickerScreen(
        viewState = params.state,
        controller = languagePickerControllerStub,
    )
}
