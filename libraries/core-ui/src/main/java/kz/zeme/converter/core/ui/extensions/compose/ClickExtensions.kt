package kz.zeme.converter.core.ui.extensions.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

fun Modifier.clickableHaptic(
    enabled: Boolean = true,
    hapticFeedbackType: HapticFeedbackType = HapticFeedbackType.TextHandleMove,
    onClick: () -> Unit,
): Modifier = composed {
    val haptic = LocalHapticFeedback.current
    clickable(
        enabled = enabled,
        onClick = {
            haptic.performHapticFeedback(hapticFeedbackType)
            onClick()
        },
    )
}

fun Modifier.clickableWithoutRipple(
    enabled: Boolean = true,
    onClick: () -> Unit,
): Modifier = composed {
    clickable(
        enabled = enabled,
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick,
    )
}
