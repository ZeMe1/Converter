package kz.zeme.converter.core.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment

abstract class BaseDialogFragment : DialogFragment() {

    open val canCancelOutside: Boolean = true
    open val canCancel: Boolean = true

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val isIntercepted = this@BaseDialogFragment.handledOnBackPressed()
            if (isIntercepted) return

            isEnabled = false
            @Suppress("DEPRECATION")
            activity?.onBackPressed()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireContext(), theme) {

            @Deprecated("Deprecated in Java")
            override fun onBackPressed() {
                val isIntercepted = this@BaseDialogFragment.handledOnBackPressed()
                if (isIntercepted) return

                @Suppress("DEPRECATION")
                super.onBackPressed()
            }
        }.apply {
            setCanceledOnTouchOutside(canCancelOutside)
            isCancelable = canCancel
            setInputMode()
        }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        registerOnBackPressedCallback()
        initViews(view)
        startObserving()
        handleArguments()
    }

    override fun getTheme(): Int = R.style.Converter_Dialog

    open fun Dialog.setInputMode() {
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    open fun handledOnBackPressed(): Boolean {
        if (canCancel) dismiss()
        return true
    }

    open fun initViews(view: View) = Unit
    open fun startObserving() = Unit
    open fun handleArguments() = Unit

    private fun registerOnBackPressedCallback() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback,
        )
    }
}
