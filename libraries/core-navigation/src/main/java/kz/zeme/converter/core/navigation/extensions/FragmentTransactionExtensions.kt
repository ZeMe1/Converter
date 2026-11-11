package kz.zeme.converter.core.navigation.extensions

import androidx.fragment.app.FragmentTransaction
import kz.zeme.converter.core.navigation.R

fun FragmentTransaction.setSlideHorizontalAnimations(): FragmentTransaction = setReorderingAllowed(true).setCustomAnimations(
    R.anim.slide_in_right,
    R.anim.slide_out_left,
    R.anim.slide_in_left,
    R.anim.slide_out_right,
)

fun FragmentTransaction.setSlideVerticalAnimations(): FragmentTransaction = setReorderingAllowed(true).setCustomAnimations(
    R.anim.bottom_sheet_slide_in,
    R.anim.bottom_sheet_slide_out,
)
