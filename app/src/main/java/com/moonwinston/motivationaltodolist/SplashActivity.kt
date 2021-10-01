package com.moonwinston.motivationaltodolist

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.moonwinston.motivationaltodolist.data.SharedManager
import com.moonwinston.motivationaltodolist.databinding.ActivitySplashBinding
import com.moonwinston.motivationaltodolist.utils.ThemeUtil

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val theme = SharedManager(this).getTheme()?:"following system"
        ThemeUtil().setTheme(theme)
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        },DURATION)

    }
    companion object {
        private const val DURATION : Long = 2000
    }
}