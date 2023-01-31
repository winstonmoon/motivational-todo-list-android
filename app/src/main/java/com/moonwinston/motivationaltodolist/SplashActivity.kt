package com.moonwinston.motivationaltodolist

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.moonwinston.motivationaltodolist.databinding.ActivitySplashBinding
import com.moonwinston.motivationaltodolist.ui.main.MainActivity
import com.moonwinston.motivationaltodolist.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivitySplashBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        ThemeUtil().setTheme(mainViewModel.themeIndex.value)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycleScope.launch(Dispatchers.Main) {
            delay(2000)
            val intent = Intent(this@SplashActivity, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            }
            startActivity(intent)
            finish()
        }
    }
}