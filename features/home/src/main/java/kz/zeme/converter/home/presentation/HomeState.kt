package kz.zeme.converter.home.presentation

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class HomeState(
    val fromCode: String = "USD",
    val fromName: String = "",
    val toCode: String = "KZT",
    val toName: String = "",
    val rawAmount: String = "",
    val isAmountError: Boolean = false,
    val formattedResultAmount: String = "",
    val formattedPairRate: String = "",
    val currenciesCount: Int = 0,
    val otherRates: ImmutableList<HomeOtherRateItem> = persistentListOf(),
)

data class HomeOtherRateItem(
    val code: String,
    val name: String,
    val formattedTotal: String,
    val formattedPairRate: String,
    val isSelected: Boolean,
)
