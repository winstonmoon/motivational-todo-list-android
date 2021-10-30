package com.moonwinston.motivationaltodolist

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.moonwinston.motivationaltodolist.data.SharedPref
import com.moonwinston.motivationaltodolist.databinding.ActivityMainBinding
import com.moonwinston.motivationaltodolist.ui.base.BaseActivity
import com.moonwinston.motivationaltodolist.utils.ThemeUtil
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {
    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)
    override val viewModel by viewModel<MainViewModel>()
    private val DAYS_FOR_FLEXIBLE_UPDATE = 4
    private val MY_REQUEST_CODE = 1
    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var listener: InstallStateUpdatedListener
    private val sharedPref: SharedPref by inject()
    private lateinit var firebaseAnalytics: FirebaseAnalytics


    override fun onCreate(savedInstanceState: Bundle?) {
        firebaseAnalytics = Firebase.analytics
        appUpdateManager = AppUpdateManagerFactory.create(this)
        listener = InstallStateUpdatedListener { state ->
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate()
            }
        }
        appUpdateManager.registerListener(listener)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && (appUpdateInfo.clientVersionStalenessDays() ?: -1) >= DAYS_FOR_FLEXIBLE_UPDATE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    this,
                    MY_REQUEST_CODE
                )
            }
        }
        super.onCreate(savedInstanceState)
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getRealSize(size)
        val width = size.x
        val height = size.y
    }

    override fun initState() {
        MobileAds.initialize(this) {}
        super.initState()
    }

    override fun initViews() = with(binding) {
        val navController = findNavController(R.id.fragment_nav_host)
        viewNav.setupWithNavController(navController)
        initSettings()
    }

    override fun observeData() {}

    override fun onResume() {
        super.onResume()
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, R.string.message_toast_update_cancel, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun popupSnackbarForCompleteUpdate() {
        Snackbar.make(
            binding.root,
            R.string.message_snack_download_complete,
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction(R.string.button_snack_restart) { appUpdateManager.completeUpdate() }
            show()
        }
    }

    private fun initSettings() {
        //TODO
        ThemeUtil().setTheme(sharedPref.getTheme())
    }
}