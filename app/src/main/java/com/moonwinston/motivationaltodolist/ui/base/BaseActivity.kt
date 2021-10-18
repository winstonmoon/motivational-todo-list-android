package com.moonwinston.motivationaltodolist.ui.base

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.SharedPref
import com.moonwinston.motivationaltodolist.utils.ContextUtil
import com.moonwinston.motivationaltodolist.utils.ThemeUtil
import kotlinx.coroutines.Job
import org.koin.android.ext.android.inject
import java.util.*

abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding> : AppCompatActivity() {

    abstract val viewModel: VM

    protected lateinit var binding: VB

    abstract fun getViewBinding(): VB

    private lateinit var fetchJob: Job

    private val sharedPref: SharedPref by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
        initState()
    }

    open fun initState() {
        initViews()
        fetchJob = viewModel.fetchData()
        observeData()
    }

    open fun initViews() = Unit

    abstract fun observeData()

    override fun onDestroy() {
        if (fetchJob.isActive) {
            fetchJob.cancel()
        }
        super.onDestroy()
    }

    override fun attachBaseContext(newBase: Context) {
        //TODO
        // get chosen language from shared preference
        val localeToSwitchTo = when (sharedPref.getLanguage()) {
            2 -> Locale.KOREAN
            1 -> Locale.ENGLISH
            else -> Locale.ENGLISH
        }
        val localeUpdatedContext: ContextWrapper =
            ContextUtil.updateLocale(newBase, localeToSwitchTo)
        super.attachBaseContext(localeUpdatedContext)
    }
}