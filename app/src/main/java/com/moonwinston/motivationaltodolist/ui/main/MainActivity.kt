package com.moonwinston.motivationaltodolist.ui.main

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
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
import com.moonwinston.motivationaltodolist.utils.getZonedEpochMilliFromOffset
import com.moonwinston.motivationaltodolist.utils.mediumFormatStyleFormatter
import com.moonwinston.motivationaltodolist.utils.setLanguage
import com.moonwinston.motivationaltodolist.utils.setNightMode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    private val MY_REQUEST_CODE = 1
    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var listener: InstallStateUpdatedListener
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var alarmManager: AlarmManager
    private var requestCodeToPendingIntent = mutableMapOf<Int, PendingIntent>()

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

        alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.fragment)
        binding.bottomNavigationView.setupWithNavController(navController)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.themeIndex.collect { themeIndex ->
                        setNightMode(themeIndex)
                    }
                }
                launch {
                    viewModel.languageIndex.collect { languageIndex ->
                        setLanguage(languageIndex)
                    }
                }
                launch {
                    viewModel.notifyIndex.collect { notifyIndex ->
                        when (Notification.values()[notifyIndex]) {
                            Notification.OFF -> cancelAlarm()
                            else -> {
                                cancelAlarm()
                                viewModel.getFutureTasks(notifyIndex)
                            }
                        }
                    }
                }
                launch {
                    viewModel.futureTasks.collect { futureTasks ->
                        when (Notification.values()[viewModel.notifyIndex.value]) {
                            Notification.OFF -> Unit
                            else -> {
                                setAlarm(
                                    notificationTime = Notification.values()[viewModel.notifyIndex.value].time,
                                    futureTasks = futureTasks
                                )
                            }
                        }
                    }
                }
            }
        }

        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                appUpdateManager
                    .appUpdateInfo
                    .addOnSuccessListener { appUpdateInfo ->
                        if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                            popupSnackbarForCompleteUpdate()
                        }
                    }
            }
        })

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

    private fun setAlarm (notificationTime: Long, futureTasks: List<TaskEntity>) {
        futureTasks.forEach { taskEntity ->
            val requestCode = taskEntity.uid.toInt()
            if (requestCodeToPendingIntent.containsKey(requestCode).not()) {
                val alarmIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
                    intent.putExtra("taskDate", taskEntity.taskDate.format(mediumFormatStyleFormatter))
                    intent.putExtra("task", taskEntity.task)
                    PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_IMMUTABLE)
                }
                val alarmTimeEpochMilli = taskEntity.taskDate.minusMinutes(notificationTime).getZonedEpochMilliFromOffset()
                alarmManager.setExact(
                    AlarmManager.RTC,
                    alarmTimeEpochMilli,
                    alarmIntent)
                requestCodeToPendingIntent[requestCode] = alarmIntent
            }
        }
    }

    private fun cancelAlarm() {
        requestCodeToPendingIntent.forEach {
            alarmManager.cancel(it.value)
        }
        requestCodeToPendingIntent.clear()
    }
}