package com.moonwinston.motivationaltodolist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.button_settings
import kotlinx.android.synthetic.main.activity_main.view_nav
import android.content.Intent
import androidx.databinding.DataBindingUtil
import com.moonwinston.motivationaltodolist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        //val navView: BottomNavigationView = findViewById(R.id.navView)
        binding.activity = this
        val navController = findNavController(R.id.fragment_nav_host)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_daily, R.id.navigation_monthly, R.id.navigation_weekly, R.id.navigation_reward, R.id.navigation_add))
        setupActionBarWithNavController(navController, appBarConfiguration)
        view_nav.setupWithNavController(navController)
        //binding.view_nav.setupWithNavController(navController)

        //binding.button_settings.setOnClickListener {
        button_settings.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}