package com.moonwinston.motivationaltodolist

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.moonwinston.motivationaltodolist.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        try {
//            Thread.sleep(5000)
//        } catch (e: InterruptedException) {
//            e.printStackTrace();
//        }
//        val intent = Intent(this, MainActivity::class.java)
//        startActivity(intent)
//        finish()
    }
}