package kz.zeme.converter.core.currency.view.presentation

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class CurrencyPickerState(
    val query: String = "",
    val items: ImmutableList<CurrencyPickerItem> = persistentListOf(),
)

data class CurrencyPickerItem(
    val code: String,
    val name: String,
    val formattedRate: String,
    val isSelected: Boolean,
)
