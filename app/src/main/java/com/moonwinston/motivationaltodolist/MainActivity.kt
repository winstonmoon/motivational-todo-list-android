package com.moonwinston.motivationaltodolist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.moonwinston.motivationaltodolist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.fragment_nav_host)
        binding.viewNav.setupWithNavController(navController)

//        binding.buttonSettings.setOnClickListener {
//            val settingsIntent = Intent(this, SettingsActivity::class.java)
//            startActivity(settingsIntent)
//        }
    }
}