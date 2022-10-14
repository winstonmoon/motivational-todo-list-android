package com.moonwinston.motivationaltodolist

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.moonwinston.motivationaltodolist.data.SharedPref
import com.moonwinston.motivationaltodolist.databinding.ActivitySplashBinding
import com.moonwinston.motivationaltodolist.utils.ThemeUtil
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val sharedPref: SharedPref by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeUtil().setTheme(sharedPref.getTheme())
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = Intent(this@SplashActivity, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        }
        GlobalScope.launch(Dispatchers.Main) {
            delay(2000)
            startActivity(intent)
            finish()
        }
    }
}