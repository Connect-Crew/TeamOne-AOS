package com.connectcrew.presentation.screen

import android.os.Bundle
import androidx.activity.viewModels
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.ActivityMainBinding
import com.connectcrew.presentation.screen.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mainViewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}