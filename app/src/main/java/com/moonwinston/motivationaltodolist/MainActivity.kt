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

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //val navView: BottomNavigationView = findViewById(R.id.navView)

        val navController = findNavController(R.id.fragment_nav_host)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_daily, R.id.navigation_monthly, R.id.navigation_weekly, R.id.navigation_add))
        setupActionBarWithNavController(navController, appBarConfiguration)
        view_nav.setupWithNavController(navController)

        button_settings.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }

    //override fun onCreate(savedInstanceState: Bundle?) {
    //    super.onCreate(savedInstanceState)
    //    setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    //    //val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
    //    //setContentView(binding.root)
    //}
}