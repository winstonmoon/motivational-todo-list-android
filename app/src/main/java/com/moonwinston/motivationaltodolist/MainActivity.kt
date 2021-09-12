package com.moonwinston.motivationaltodolist

import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.moonwinston.motivationaltodolist.databinding.ActivityMainBinding
import com.moonwinston.motivationaltodolist.ui.base.BaseActivity
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {
    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)
    override val viewModel by viewModel<MainViewModel>()

    override fun initViews() = with(binding) {
        val navController = findNavController(R.id.fragment_nav_host)
        viewNav.setupWithNavController(navController)
    }

    override fun observeData() {}
}