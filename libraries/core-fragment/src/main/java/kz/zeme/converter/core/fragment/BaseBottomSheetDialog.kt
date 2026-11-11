package kz.zeme.converter.core.fragment

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.R.id.design_bottom_sheet as design_bottom_sheet_id

abstract class BaseBottomSheetDialog : BottomSheetDialogFragment() {

    open val isFullScreen: Boolean = false
    open val isExpanded: Boolean = false
    open val isCancellable: Boolean = true
    open val skipHalfCollapse: Boolean = false
    open val softInputMode: Int? = null

    private val bottomSheetBehavior
        get() = bottomSheet?.let { BottomSheetBehavior.from(it) }

    private val bottomSheet
        get() = dialog?.findViewById<View>(design_bottom_sheet_id)

    var bottomSheetState: Int = BottomSheetBehavior.STATE_EXPANDED
        set(value) {
            bottomSheetBehavior?.state = value
            field = value
        }

    override fun getTheme(): Int = R.style.BottomSheetDialog_Theme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = isCancellable
    }

    override fun onCreateDialog(
        savedInstanceState: Bundle?,
    ): Dialog = object : BottomSheetDialog(requireContext(), theme) {

        @Deprecated("Deprecated in Java")
        override fun onBackPressed() {
            val isIntercepted = this@BaseBottomSheetDialog.handledOnBackPressed()
            if (isIntercepted) return

            @Suppress("DEPRECATION")
            super.onBackPressed()
        }

    }
        .setExpandedState()
        .setSoftInputMode()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        startObserving()
        handleArguments()
    }

    open fun handledOnBackPressed(): Boolean = false
    open fun initViews(view: View) = Unit
    open fun startObserving() = Unit
    open fun handleArguments() = Unit

    private fun BottomSheetDialog.setExpandedState() = apply {
        setOnShowListener {
            bottomSheetBehavior?.setExpandedState()
        }
    }

    private fun BottomSheetBehavior<View>.setExpandedState() {
        if (isFullScreen) {
            peekHeight = getWindowHeight()
            bottomSheet?.setupFullHeight()
        }
        if (isExpanded || isFullScreen) state = BottomSheetBehavior.STATE_EXPANDED
        skipCollapsed = this@BaseBottomSheetDialog.skipHalfCollapse
    }

    private fun View.setupFullHeight() = apply {
        val params = layoutParams as? CoordinatorLayout.LayoutParams ?: return@apply
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams = params
    }

    private fun getWindowHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    private fun BottomSheetDialog.setSoftInputMode() = apply {
        softInputMode?.let {
            window?.setSoftInputMode(it)
        }
    }
}
