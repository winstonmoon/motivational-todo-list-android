package com.moonwinston.motivationaltodolist.ui.main

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
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
import com.moonwinston.motivationaltodolist.ui.base.BaseActivity
import com.moonwinston.motivationaltodolist.utils.Notification
import com.moonwinston.motivationaltodolist.utils.getEpoch
import com.moonwinston.motivationaltodolist.utils.setLanguage
import com.moonwinston.motivationaltodolist.utils.setNightMode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class MainActivity: BaseActivity<ActivityMainBinding, MainViewModel>() {
    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)
    override val viewModel: MainViewModel by viewModels()
    private val MY_REQUEST_CODE = 1
    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var listener: InstallStateUpdatedListener
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var alarmManager: AlarmManager
    private var requestCodes = mutableListOf<Int>()

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
    }

    override fun initViews() {
        val navController = findNavController(R.id.fragment)
        binding.bottomNavigationView.setupWithNavController(navController)
    }
    override fun initListeners() {
    }
    override fun initObservers() {
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
                            Notification.OFF -> cancelAlarm(requestCodes)
                            else -> viewModel.getFutureTasks(notifyIndex)
                        }
                    }
                }
                launch {
                    viewModel.futureTasks.collect { futureTasks ->
                        when (Notification.values()[viewModel.notifyIndex.value]) {
                            Notification.OFF -> Unit
                            else -> {
                                setAlarm(
                                    notificationTime = viewModel.notifyIndex.value,
                                    futureTasks = futureTasks
                                )
                            }
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
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        futureTasks.forEach { taskEntity ->
            val requestCode = taskEntity.taskDate.getEpoch().toInt()
            val alarmIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
                intent.putExtra("task", taskEntity.task)
                intent.putExtra("taskDate", taskEntity.taskDate.format(formatter))
                PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_IMMUTABLE)
            }
            alarmManager.setExact(
                AlarmManager.RTC,
                taskEntity.taskDate.minusMinutes(notificationTime.toLong()).getEpoch(),
                alarmIntent)
            requestCodes.add(requestCode)
        }
    }

    private fun cancelAlarm(requestCodes: List<Int>) {
        requestCodes.forEach { requestCode ->
            val pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_IMMUTABLE)
            alarmManager.cancel(pendingIntent)
        }
    }
}