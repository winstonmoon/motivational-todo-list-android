package com.moonwinston.motivationaltodolist.ui.splash

import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.moonwinston.motivationaltodolist.databinding.ActivitySplashBinding
import com.moonwinston.motivationaltodolist.ui.base.BaseActivity
import com.moonwinston.motivationaltodolist.ui.main.MainActivity
import com.moonwinston.motivationaltodolist.ui.main.MainViewModel
import com.moonwinston.motivationaltodolist.utils.setNightMode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class SplashActivity: BaseActivity<ActivitySplashBinding, MainViewModel>() {
    override fun getViewBinding() = ActivitySplashBinding.inflate(layoutInflater)
    override val viewModel: MainViewModel by viewModels()

    override fun initViews() {
        lifecycleScope.launch(Dispatchers.Main) {
            delay(2000)
            val intent = Intent(this@SplashActivity, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            }
            startActivity(intent)
            finish()
        }
    }
    override fun initListeners() {
    }
    override fun initObservers() {
        lifecycleScope.launch {
            viewModel.themeIndex.collect { themeIndex ->
                setNightMode(themeIndex)
            }
        }
    }
}