package kz.zeme.converter.core.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val isIntercepted = this@BaseFragment.handledOnBackPressed()
            if (isIntercepted) return

            isEnabled = false
            @Suppress("DEPRECATION")
            activity?.onBackPressed()
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
        handleArguments(savedInstanceState, arguments)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
        childFragmentManager.fragments.forEach {
            @Suppress("DEPRECATION")
            it.onActivityResult(requestCode, resultCode, data)
        }
    }

    open fun handledOnBackPressed(): Boolean = false
    open fun initViews(view: View) = Unit
    open fun startObserving() = Unit
    open fun handleArguments(savedInstanceState: Bundle?, arguments: Bundle?) = Unit
    open fun onRedirect(bundle: Bundle?) = Unit
    open fun onRedirect(intent: Intent?) = Unit

    private fun registerOnBackPressedCallback() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback,
        )
    }
}
