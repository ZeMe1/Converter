package kz.zeme.converter.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import kz.zeme.converter.core.res.theme.bold
import kz.zeme.converter.core.res.theme.medium
import kz.zeme.converter.core.res.theme.primary
import kz.zeme.converter.core.res.theme.secondary
import kz.zeme.converter.core.ui.components.DefaultErrorScreen
import kz.zeme.converter.core.ui.compose.views.TextField
import kz.zeme.converter.core.ui.extensions.compose.clickableHaptic
import kz.zeme.converter.core.ui.extensions.shimmer

@Composable
fun HomeScreen(
    state: State<ViewState<HomeState>?>,
    controller: HomeController,
) {
    HomeScreen(
        viewState = state.value,
        controller = controller,
    )
}

@Composable
private fun HomeScreen(
    viewState: ViewState<HomeState>?,
    controller: HomeController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ConverterTheme.colors.mainBg)
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        HomeTopBar(onLanguageClick = controller::onOpenLanguagePickerClick)

        when (viewState) {
            null,
            is ViewState.Loading.Skeleton -> HomeShimmerView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp),
            )

            is ViewState.Error -> DefaultErrorView(
                modifier = Modifier.fillMaxSize(),
                error = viewState.error,
                onRefresh = controller::onRefresh,
            )

            is ViewState.Loading.ProgressBar,
            is ViewState.Data -> viewState.data?.let {
                HomeContent(
                    state = it,
                    controller = controller,
                )
            } ?: HomeShimmerView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp),
            )
        }
    }
}

@Composable
private fun HomeTopBar(
    onLanguageClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.home_title),
            style = ConverterTheme.typography.titleLarge.primary(),
        )

        Icon(
            modifier = Modifier
                .size(24.dp)
                .clickableHaptic(onClick = onLanguageClick),
            painter = painterResource(R.drawable.ic_globe),
            contentDescription = null,
            tint = ConverterTheme.colors.textPrimary,
        )
    }
}

@Composable
private fun HomeContent(
    state: HomeState,
    controller: HomeController,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 24.dp),
    ) {
        item(key = "converter_section") {
            ConverterSection(
                state = state,
                controller = controller,
            )
        }

        item(key = "section_header") {
            SectionHeader(
                currenciesCount = state.currenciesCount,
            )
        }

        items(
            items = state.otherRates,
            key = { it.code },
        ) { item ->
            OtherRateCard(
                item = item,
                onCurrencyClick = controller::onOtherRateCurrencyClick,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
    }
}

@Composable
private fun ConverterSection(
    state: HomeState,
    controller: HomeController,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
    ) {
        FromCurrencySection(
            code = state.fromCode,
            name = state.fromName,
            rawAmount = state.rawAmount,
            isAmountError = state.isAmountError,
            onPickCurrency = controller::onPickFromCurrencyClick,
            onAmountChange = controller::onAmountChange,
        )

        SwapRow(onSwapClick = controller::onSwapCurrenciesClick)

        ToCurrencySection(
            code = state.toCode,
            name = state.toName,
            formattedResult = state.formattedResultAmount,
            formattedPairRate = state.formattedPairRate,
            onPickCurrency = controller::onPickToCurrencyClick,
        )
    }
}

@Composable
private fun FromCurrencySection(
    code: String,
    name: String,
    rawAmount: String,
    isAmountError: Boolean,
    onPickCurrency: () -> Unit,
    onAmountChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        CurrencySelectBar(
            code = code,
            name = name,
            onPickCurrency = onPickCurrency,
        )

        TextField(
            modifier = Modifier.fillMaxWidth(),
            minHeight = 44.dp,
            text = rawAmount,
            onValueChange = onAmountChange,
            hint = stringResource(R.string.home_amount_hint),
            maxLines = 1,
            containerColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            errorBorderColor = Color.Transparent,
            contentPadding = PaddingValues(vertical = 4.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done,
            ),
            textStyle = ConverterTheme.typography.titleLarge.primary().copy(
                textAlign = TextAlign.End,
            ),
            hintStyle = ConverterTheme.typography.titleLarge.secondary().copy(
                textAlign = TextAlign.End,
            ),
            error = if (isAmountError) stringResource(R.string.shared_max_char, 12) else null,
        )
    }
}

@Composable
private fun SwapRow(
    onSwapClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = ConverterTheme.colors.buttonDisabled.copy(alpha = 0.6f),
            thickness = 1.dp,
        )

        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = ConverterTheme.colors.textSecondary.copy(alpha = 0.22f),
                    shape = CircleShape,
                )
                .background(ConverterTheme.colors.basicDark)
                .clickableHaptic(onClick = onSwapClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.ic_swap_vert),
                contentDescription = null,
                tint = ConverterTheme.colors.textPrimary,
            )
        }

        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = ConverterTheme.colors.buttonDisabled.copy(alpha = 0.6f),
            thickness = 1.dp,
        )
    }
}

@Composable
private fun ToCurrencySection(
    code: String,
    name: String,
    formattedResult: String,
    formattedPairRate: String,
    onPickCurrency: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val clipboardManager = LocalClipboardManager.current
    val onCopyClick = remember(formattedResult) {
        {
            clipboardManager.setText(AnnotatedString(formattedResult))
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        CurrencySelectBar(
            code = code,
            name = name,
            onPickCurrency = onPickCurrency,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f, fill = false),
                text = formattedResult,
                style = ConverterTheme.typography.titleLarge.primary(),
                textAlign = TextAlign.End,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(
                        width = 1.dp,
                        color = ConverterTheme.colors.textSecondary.copy(alpha = 0.22f),
                        shape = RoundedCornerShape(10.dp),
                    )
                    .background(ConverterTheme.colors.basicDark)
                    .clickableHaptic(onClick = onCopyClick),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_copy),
                    contentDescription = null,
                    tint = ConverterTheme.colors.textSecondary,
                )
            }
        }

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = formattedPairRate,
            style = ConverterTheme.typography.bodySmall.secondary(),
            textAlign = TextAlign.End,
        )
    }
}

@Composable
private fun CurrencySelectBar(
    code: String,
    name: String,
    onPickCurrency: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier
                .height(36.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = ConverterTheme.colors.textSecondary.copy(alpha = 0.22f),
                    shape = CircleShape,
                )
                .background(ConverterTheme.colors.basicDark)
                .clickableHaptic(onClick = onPickCurrency)
                .padding(start = 14.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = code,
                style = ConverterTheme.typography.titleMedium.primary(),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.ic_chevron_down),
                contentDescription = null,
                tint = ConverterTheme.colors.textSecondary,
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            modifier = Modifier.weight(1f, fill = false),
            text = name,
            style = ConverterTheme.typography.bodySmall.secondary(),
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun SectionHeader(
    currenciesCount: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.home_other_rates),
            style = ConverterTheme.typography.titleSmall.medium().primary(),
        )

        Text(
            text = pluralStringResource(
                R.plurals.home_currencies_count,
                currenciesCount,
                currenciesCount,
            ),
            style = ConverterTheme.typography.bodySmall.secondary(),
        )
    }
}

@Composable
private fun OtherRateCard(
    item: HomeOtherRateItem,
    onCurrencyClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val onClick = remember(item.code, onCurrencyClick) {
        { onCurrencyClick(item.code) }
    }

    val borderModifier = if (item.isSelected) {
        Modifier.border(
            width = 1.dp,
            color = ConverterTheme.colors.textSecondary.copy(alpha = 0.65f),
            shape = RoundedCornerShape(12.dp),
        )
    } else {
        Modifier
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .then(borderModifier)
            .background(ConverterTheme.colors.basicDark)
            .clickableHaptic(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.code,
                style = ConverterTheme.typography.titleSmall.bold().primary(),
            )
            Text(
                text = item.name,
                style = ConverterTheme.typography.bodySmall.secondary(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = item.formattedTotal,
                style = ConverterTheme.typography.titleSmall.bold().primary(),
                textAlign = TextAlign.End,
            )
            Text(
                text = item.formattedPairRate,
                style = ConverterTheme.typography.bodySmall.secondary(),
                textAlign = TextAlign.End,
            )
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
        modifier = modifier,
        title = stringResource(R.string.home_error_title),
        errorMessage = error?.message ?: stringResource(R.string.shared_something_went_wrong),
        onRefresh = onRefresh,
    )
}

@Composable
private fun HomeShimmerView(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(
                    modifier = Modifier
                        .size(width = 84.dp, height = 36.dp)
                        .clip(CircleShape)
                        .background(ConverterTheme.colors.mainShimmer)
                        .shimmer(),
                )
                Spacer(
                    modifier = Modifier
                        .size(width = 96.dp, height = 14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(ConverterTheme.colors.mainShimmer)
                        .shimmer(),
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .height(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(ConverterTheme.colors.mainShimmer)
                    .shimmer(),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = ConverterTheme.colors.buttonDisabled.copy(alpha = 0.6f),
                    thickness = 1.dp,
                )
                Spacer(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(ConverterTheme.colors.mainShimmer)
                        .shimmer(),
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = ConverterTheme.colors.buttonDisabled.copy(alpha = 0.6f),
                    thickness = 1.dp,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(
                    modifier = Modifier
                        .size(width = 84.dp, height = 36.dp)
                        .clip(CircleShape)
                        .background(ConverterTheme.colors.mainShimmer)
                        .shimmer(),
                )
                Spacer(
                    modifier = Modifier
                        .size(width = 118.dp, height = 14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(ConverterTheme.colors.mainShimmer)
                        .shimmer(),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(
                    modifier = Modifier
                        .size(width = 144.dp, height = 32.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(ConverterTheme.colors.mainShimmer)
                        .shimmer(),
                )
                Spacer(modifier = Modifier.width(10.dp))
                Spacer(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(ConverterTheme.colors.mainShimmer)
                        .shimmer(),
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Spacer(
                    modifier = Modifier
                        .size(width = 128.dp, height = 14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(ConverterTheme.colors.mainShimmer)
                        .shimmer(),
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(
                modifier = Modifier
                    .size(width = 140.dp, height = 20.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(ConverterTheme.colors.mainShimmer)
                    .shimmer(),
            )
            Spacer(
                modifier = Modifier
                    .size(width = 60.dp, height = 16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(ConverterTheme.colors.mainShimmer)
                    .shimmer(),
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            repeat(5) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ConverterTheme.colors.basicDark)
                        .shimmer(),
                )
            }
        }
    }
}

private val homeControllerStub = object : HomeController {
    override fun onStart() = Unit
    override fun onRefresh() = Unit
    override fun onAmountChange(amount: String) = Unit
    override fun onSwapCurrenciesClick() = Unit
    override fun onOpenLanguagePickerClick() = Unit
    override fun onPickFromCurrencyClick() = Unit
    override fun onPickToCurrencyClick() = Unit
    override fun onOtherRateCurrencyClick(code: String) = Unit
}

private val homeStateStub = HomeState(
    fromCode = "USD",
    fromName = "Доллар США",
    toCode = "KZT",
    toName = "Казахстанский тенге",
    rawAmount = "",
    formattedResultAmount = "0.00",
    formattedPairRate = "1 USD = 490.25 KZT",
    currenciesCount = 11,
    otherRates = persistentListOf(
        HomeOtherRateItem(
            code = "KZT",
            name = "Казахстанский тенге",
            formattedTotal = "490 250.00 KZT",
            formattedPairRate = "1 USD = 490.25 KZT",
            isSelected = true,
        ),
        HomeOtherRateItem(
            code = "EUR",
            name = "Евро",
            formattedTotal = "921.50 EUR",
            formattedPairRate = "1 USD = 0.9215 EUR",
            isSelected = false,
        ),
        HomeOtherRateItem(
            code = "RUB",
            name = "Российский рубль",
            formattedTotal = "97 400.00 RUB",
            formattedPairRate = "1 USD = 97.4 RUB",
            isSelected = false,
        ),
        HomeOtherRateItem(
            code = "GBP",
            name = "Фунт стерлингов",
            formattedTotal = "785.10 GBP",
            formattedPairRate = "1 USD = 0.7851 GBP",
            isSelected = false,
        ),
    ),
)

private data class HomeScreenPreviewParameter(
    val state: ViewState<HomeState>,
)

private class HomeScreenPreviewParameterProvider : PreviewParameterProvider<HomeScreenPreviewParameter> {

    override val values: Sequence<HomeScreenPreviewParameter> = sequenceOf(
        HomeScreenPreviewParameter(state = ViewState.Data(homeStateStub)),
        HomeScreenPreviewParameter(state = ViewState.Data(homeStateStub.copy(rawAmount = "1000", formattedResultAmount = "490 250.00"))),
        HomeScreenPreviewParameter(state = ViewState.Data(homeStateStub.copy(rawAmount = "1234567890123", isAmountError = true))),
        HomeScreenPreviewParameter(state = ViewState.Loading.Skeleton()),
        HomeScreenPreviewParameter(state = ViewState.Error(UnexpectedError())),
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF010101,
)
@Composable
private fun HomeScreenPreview(
    @PreviewParameter(HomeScreenPreviewParameterProvider::class)
    params: HomeScreenPreviewParameter,
) {
    HomeScreen(
        viewState = params.state,
        controller = homeControllerStub,
    )
}
