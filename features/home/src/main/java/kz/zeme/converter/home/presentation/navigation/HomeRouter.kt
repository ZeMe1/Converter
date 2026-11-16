package kz.zeme.converter.home.presentation.navigation

import kz.zeme.converter.home.presentation.HomeFragment

class HomeRouter {

    companion object {
        const val TAG_HOME = "TAG_HOME"
    }

    fun getHomeFragment(): HomeFragment = HomeFragment()
}
