package com.connectcrew.presentation.screen.feature.intro

import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.ActivityIntroBinding
import com.connectcrew.presentation.screen.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroActivity : BaseActivity<ActivityIntroBinding>(R.layout.activity_intro) {

    private val navController : NavController
        get() = findNavController(R.id.nav_intro)

    override fun onSupportNavigateUp(): Boolean = navController.navigateUp()
}