package com.moonwinston.motivationaltodolist

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
import com.moonwinston.motivationaltodolist.databinding.ActivityMainBinding
import com.moonwinston.motivationaltodolist.ui.common.SharedViewModel
import com.moonwinston.motivationaltodolist.utils.ContextUtil
import com.moonwinston.motivationaltodolist.utils.ThemeUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val sharedViewModel: SharedViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private val MY_REQUEST_CODE = 1
    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var listener: InstallStateUpdatedListener
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        firebaseAnalytics = Firebase.analytics
        MobileAds.initialize(this) {}

        ThemeUtil().setTheme(sharedViewModel.themeIndex.value)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.fragment)
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun attachBaseContext(newBase: Context) {
//        sharedViewModel.getLanguage()
//        val language = sharedViewModel.languageIndex.value
//        val localeUpdatedContext: ContextWrapper =
//            ContextUtil.updateLocale(newBase, language)
        val localeUpdatedContext: ContextWrapper =
            ContextUtil.updateLocale(newBase, 1)
        super.attachBaseContext(localeUpdatedContext)
    }

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
}