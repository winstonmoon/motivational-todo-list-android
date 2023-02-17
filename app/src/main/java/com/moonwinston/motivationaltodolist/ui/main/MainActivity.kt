package com.moonwinston.motivationaltodolist.ui.main

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.ActivityMainBinding
import com.moonwinston.motivationaltodolist.receiver.AlarmReceiver
import com.moonwinston.motivationaltodolist.utils.Notification
import com.moonwinston.motivationaltodolist.utils.getEpoch
import com.moonwinston.motivationaltodolist.utils.setLanguage
import com.moonwinston.motivationaltodolist.utils.setNightMode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private val MY_REQUEST_CODE = 1
    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var listener: InstallStateUpdatedListener
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var alarmManager: AlarmManager
//    private lateinit var alarmIntents: MutableList<PendingIntent>
    private var alarmIntents = mutableListOf<PendingIntent>()

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
//        MobileAds.initialize(this) {}

        alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.fragment)
        binding.bottomNavigationView.setupWithNavController(navController)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.themeIndex.collect { themeIndex ->
                    setNightMode(themeIndex)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.languageIndex.collect { languageIndex ->
                    setLanguage(languageIndex)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.notifyIndex.collect { notifyIndex ->
                    when (Notification.values()[notifyIndex]) {
                        Notification.OFF -> cancelAlarm(alarmIntents)
                        else -> mainViewModel.getFutureTasks(notifyIndex)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.futureTasks.collect { futureTasks ->
                    when (Notification.values()[mainViewModel.notifyIndex.value]) {
                        Notification.OFF -> Unit
                        else -> {
                            cancelAlarm(alarmIntents)
                            setAlarm(
                                notificationTime = mainViewModel.notifyIndex.value,
                                futureTasks = futureTasks
                            )
                        }
                    }
                }
            }
        }
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

    private fun setAlarm (notificationTime: Int, futureTasks: List<TaskEntity>) {
        alarmIntents.clear()
        var requestCode = 0
        futureTasks.forEach { taskEntity ->
            val alarmIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
                intent.putExtra("task", taskEntity.task)
                intent.putExtra("taskDate", taskEntity.taskDate)
                PendingIntent.getBroadcast(this, requestCode, intent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }
            alarmManager.setExact(
                AlarmManager.RTC,
                taskEntity.taskDate.minusMinutes(notificationTime.toLong()).getEpoch(),
                alarmIntent)
            alarmIntents.add(alarmIntent)
            requestCode =+ 1
        }
    }

    private fun cancelAlarm(alarmIntents: List<PendingIntent>) {
        alarmIntents.forEach { alarmIntent ->
            alarmManager.cancel(alarmIntent)
        }
    }
}