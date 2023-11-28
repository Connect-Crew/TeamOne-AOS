package com.connectcrew.presentation.screen

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.ActivityMainBinding
import com.connectcrew.presentation.screen.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private var currentNavId = NAV_ID_NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initListener()
    }

    private fun initView() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        dataBinding.bottomNav.apply {
            setupWithNavController(navController)
            itemIconTintList = null
        }
    }

    private fun initListener() {
        navController.addOnDestinationChangedListener { navController, destination, _ ->
            currentNavId = destination.id
            dataBinding.bottomNav.isVisible = if (destination.label == resources.getString(R.string.label_dialog)) {
                TOP_LEVEL_DESTINATIONS.any { it == navController.previousBackStackEntry?.destination?.id }
            } else {
                TOP_LEVEL_DESTINATIONS.any { it == destination.id }
            }
        }

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentNavId = navController.currentDestination?.id ?: NAV_ID_NONE
    }

    companion object {
        private val TOP_LEVEL_DESTINATIONS = listOf(
            R.id.homeFragment,
            R.id.myTeamFragment,
            R.id.myPageFragment
        )

        private const val NAV_ID_NONE = -1
    }
}