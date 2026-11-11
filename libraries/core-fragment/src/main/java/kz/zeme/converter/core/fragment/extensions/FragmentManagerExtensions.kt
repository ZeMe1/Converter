package kz.zeme.converter.core.fragment.extensions

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kz.zeme.converter.core.fragment.BaseFragment

inline fun <reified T : Fragment> FragmentManager.isVisible() =
    fragments.filterIsInstance<T>().firstOrNull()?.isVisible == true

fun FragmentManager.isShown(tag: String): Boolean = findFragmentByTag(tag) != null

fun FragmentManager.dismiss(tag: String) =
    (findFragmentByTag(tag) as? DialogFragment)?.dismiss()

fun FragmentManager.close(tag: String) {
    val fragment = findFragmentByTag(tag) ?: return
    if (fragment is DialogFragment) fragment.dismiss()
    else popBackStack()
}

fun FragmentManager.onBackPressed() {
    val dialogFragments = fragments.filterIsInstance<DialogFragment>()
    dialogFragments.lastOrNull()?.dismiss() ?: popBackStack()
}

fun FragmentManager.onRedirect(bundle: Bundle?) {
    fragments.forEach {
        (it as? BaseFragment)?.onRedirect(bundle)
    }
}

fun FragmentManager.onRedirect(intent: Intent?) {
    fragments.forEach {
        (it as? BaseFragment)?.onRedirect(intent)
    }
}
