package kz.zeme.converter.core.currency.view.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kz.zeme.converter.core.error.BaseError
import kz.zeme.converter.core.error.UnexpectedError
import kz.zeme.converter.core.observable.ViewState
import kz.zeme.converter.core.res.R
import kz.zeme.converter.core.res.theme.ConverterTheme
import kz.zeme.converter.core.res.theme.primary
import kz.zeme.converter.core.res.theme.secondary
import kz.zeme.converter.core.ui.components.DefaultErrorScreen
import kz.zeme.converter.core.ui.compose.views.ModalBottomSheet
import kz.zeme.converter.core.ui.compose.views.TextField
import kz.zeme.converter.core.ui.extensions.compose.clickableHaptic
import kz.zeme.converter.core.ui.extensions.shimmer

@Composable
fun CurrencyPickerScreen(
    state: State<ViewState<CurrencyPickerState>?>,
    controller: CurrencyPickerController,
) {
    CurrencyPickerScreen(
        viewState = state.value,
        controller = controller,
    )
}

@Composable
private fun CurrencyPickerScreen(
    viewState: ViewState<CurrencyPickerState>?,
    controller: CurrencyPickerController,
) {
    ModalBottomSheet(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            text = stringResource(R.string.currency_picker_title),
            style = ConverterTheme.typography.titleMedium.primary(),
        )

        when (viewState) {
            null,
            is ViewState.Loading.Skeleton -> CurrencyPickerShimmerView()

            is ViewState.Error -> DefaultErrorView(
                error = viewState.error,
                onRefresh = controller::onRefresh,
            )

            is ViewState.Loading.ProgressBar,
            is ViewState.Data -> viewState.data?.let {
                CurrencyPickerContent(
                    state = it,
                    controller = controller,
                )
            } ?: CurrencyPickerShimmerView()
        }
    }
}

@Composable
private fun ColumnScope.CurrencyPickerContent(
    state: CurrencyPickerState,
    controller: CurrencyPickerController,
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 4.dp, bottom = 12.dp),
        minHeight = 48.dp,
        text = state.query,
        hint = stringResource(R.string.currency_picker_search_hint),
        maxLines = 1,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        iconStart = {
            Icon(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(24.dp),
                painter = painterResource(R.drawable.ic_search),
                contentDescription = null,
                tint = ConverterTheme.colors.textSecondary,
            )
        },
        iconEnd = if (state.query.isNotEmpty()) {
            {
                Icon(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(24.dp)
                        .clickableHaptic(onClick = controller::onClearSearch),
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = null,
                    tint = ConverterTheme.colors.textSecondary,
                )
            }
        } else null,
        onValueChange = controller::onQueryUpdate,
    )

    CurrencyListContent(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f, fill = false),
        items = state.items,
        controller = controller,
    )
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
            .height(240.dp),
        errorMessage = error?.message ?: stringResource(R.string.shared_something_went_wrong),
        onRefresh = onRefresh,
        backgroundColor = ConverterTheme.colors.basicDark,
    )
}

@Composable
private fun CurrencyPickerShimmerView(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        repeat(6) {
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
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Spacer(
                        modifier = Modifier
                            .width(48.dp)
                            .height(16.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(ConverterTheme.colors.mainShimmer)
                            .shimmer(),
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth(0.55f)
                            .height(12.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(ConverterTheme.colors.mainShimmer)
                            .shimmer(),
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Spacer(
                    modifier = Modifier
                        .width(56.dp)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(ConverterTheme.colors.mainShimmer)
                        .shimmer(),
                )
            }
        }
    }
}

@Composable
private fun CurrencyListContent(
    modifier: Modifier = Modifier,
    items: ImmutableList<CurrencyPickerItem>,
    controller: CurrencyPickerController,
) {
    if (items.isEmpty()) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 28.dp),
            text = stringResource(R.string.currency_picker_empty_search),
            style = ConverterTheme.typography.bodyMedium.secondary(),
            textAlign = TextAlign.Center,
        )
        return
    }

    LazyColumn(
        modifier = modifier.padding(bottom = 12.dp),
    ) {
        currencyPickerItems(
            items = items,
            onItemClick = controller::onCurrencyClick,
        )
    }
}

private fun LazyListScope.currencyPickerItems(
    items: ImmutableList<CurrencyPickerItem>,
    onItemClick: (CurrencyPickerItem) -> Unit,
) {
    items(items = items) { item ->
        CurrencyItemRow(
            item = item,
            onClick = { onItemClick(item) },
        )

        if (items.lastOrNull() != item) {
            HorizontalDivider(
                modifier = Modifier.padding(start = 16.dp),
                color = ConverterTheme.colors.buttonDisabled.copy(alpha = 0.5f),
                thickness = 1.dp,
            )
        }
    }
}

@Composable
private fun CurrencyItemRow(
    item: CurrencyPickerItem,
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
                .alpha(if (item.isSelected) 1f else 0f),
            painter = painterResource(R.drawable.ic_check_mark),
            contentDescription = null,
            tint = ConverterTheme.colors.textPrimary,
        )

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = item.code,
                style = ConverterTheme.typography.bodyLarge.primary(),
            )
            Text(
                text = item.name,
                style = ConverterTheme.typography.bodySmall.secondary(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = item.formattedRate,
            style = ConverterTheme.typography.bodySmall.secondary(),
        )
    }
}

private val currencyPickerControllerStub = object : CurrencyPickerController {
    override fun onStart() = Unit
    override fun onRefresh() = Unit
    override fun onQueryUpdate(query: String) = Unit
    override fun onClearSearch() = Unit
    override fun onCurrencyClick(item: CurrencyPickerItem) = Unit
}

private val currencyPickerStateStub = CurrencyPickerState(
    query = "",
    items = persistentListOf(
        CurrencyPickerItem(
            code = "KZT",
            name = "Kazakhstani Tenge",
            formattedRate = "490.25",
            isSelected = true,
        ),
        CurrencyPickerItem(
            code = "USD",
            name = "United States Dollar",
            formattedRate = "1",
            isSelected = false,
        ),
        CurrencyPickerItem(
            code = "EUR",
            name = "Euro",
            formattedRate = "0.9215",
            isSelected = false,
        ),
        CurrencyPickerItem(
            code = "RUB",
            name = "Russian Ruble",
            formattedRate = "97.4",
            isSelected = false,
        ),
    ),
)

private data class CurrencyPickerScreenPreviewParameter(
    val state: ViewState<CurrencyPickerState>,
)

private class CurrencyPickerScreenPreviewParameterProvider :
    PreviewParameterProvider<CurrencyPickerScreenPreviewParameter> {

    override val values: Sequence<CurrencyPickerScreenPreviewParameter> = sequenceOf(
        CurrencyPickerScreenPreviewParameter(
            state = ViewState.Data(currencyPickerStateStub),
        ),
        CurrencyPickerScreenPreviewParameter(
            state = ViewState.Data(CurrencyPickerState(query = "XYZ")),
        ),
        CurrencyPickerScreenPreviewParameter(
            state = ViewState.Loading.Skeleton(),
        ),
        CurrencyPickerScreenPreviewParameter(
            state = ViewState.Error(UnexpectedError()),
        ),
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF010101,
)
@Composable
private fun CurrencyPickerScreenPreview(
    @PreviewParameter(CurrencyPickerScreenPreviewParameterProvider::class)
    params: CurrencyPickerScreenPreviewParameter,
) {
    CurrencyPickerScreen(
        viewState = params.state,
        controller = currencyPickerControllerStub,
    )
}
