package com.moonwinston.motivationaltodolist.ui.settings

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.moonwinston.motivationaltodolist.BuildConfig
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.FragmentSettingsBinding
import com.moonwinston.motivationaltodolist.receiver.AlarmReceiver
import com.moonwinston.motivationaltodolist.ui.main.MainViewModel
import com.moonwinston.motivationaltodolist.utils.getEpoch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var alarmManager: AlarmManager
    private lateinit var alarmIntent: PendingIntent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, FLAG_IMMUTABLE)
        }

        binding.notify.text = resources.getStringArray(R.array.notify_array)[mainViewModel.notifyIndex.value]
        binding.theme.text = resources.getStringArray(R.array.theme_array)[mainViewModel.themeIndex.value]
        binding.language.text = resources.getStringArray(R.array.language_array)[mainViewModel.languageIndex.value]
        binding.versionTextView.text = BuildConfig.VERSION_NAME

        binding.backButton.setOnClickListener {
            it.findNavController().popBackStack()
        }
        binding.notifyButton.setOnClickListener {
            val builder = AlertDialog.Builder(it.context, R.style.CustomAlertDialog)
            val notifyItems = resources.getStringArray(R.array.notify_array)
            val checkedItem = mainViewModel.notifyIndex.value
            builder.setTitle(resources.getString(R.string.label_notify))
                .setSingleChoiceItems(notifyItems, checkedItem) { dialog, which ->
                    binding.notify.text = notifyItems[which]
                    mainViewModel.setNotify(which)
                    settingsViewModel.setNotification(which)
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.button_cancel) { dialog, _ ->
                    dialog?.cancel()
                }
            builder.show()
        }
        binding.themeButton.setOnClickListener {
            val builder = AlertDialog.Builder(it.context, R.style.CustomAlertDialog)
            val themeItems = resources.getStringArray(R.array.theme_array)
            val checkedItem = mainViewModel.themeIndex.value
            builder.setTitle(resources.getString(R.string.label_theme))
                .setSingleChoiceItems(themeItems, checkedItem) { dialog, which ->
                    binding.theme.text = themeItems[which]
                    mainViewModel.setTheme(which)
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.button_cancel) { dialog, _ ->
                    dialog?.cancel()
                }
            builder.show()
        }
        binding.languageButton.setOnClickListener {
            val builder = AlertDialog.Builder(it.context, R.style.CustomAlertDialog)
            val languageItems = resources.getStringArray(R.array.language_array)
            val checkedItem = mainViewModel.languageIndex.value
            builder.setTitle(resources.getString(R.string.label_language))
                .setSingleChoiceItems(languageItems, checkedItem) { dialog, which ->
                    binding.language.text = languageItems[which]
                    mainViewModel.setLanguage(which)
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.button_cancel) { dialog, _ ->
                    dialog?.cancel()
                }
            builder.show()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                settingsViewModel.notificationTime.collect { notificationTime ->
                    if (notificationTime == 0L) {
                        cancelAlarm(settingsViewModel.alarmIntents.value)
                    } else {
                        cancelAlarm(settingsViewModel.alarmIntents.value)
                        setAlarm(
                            notificationTime = settingsViewModel.notificationTime.value,
                            futureTasks = settingsViewModel.futureTasks.value
                        )
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                settingsViewModel.futureTasks.collect { futureTasks ->
                    if (settingsViewModel.notificationTime.value != 0L) {
                        cancelAlarm(settingsViewModel.alarmIntents.value)
                        setAlarm(
                            notificationTime = settingsViewModel.notificationTime.value,
                            futureTasks = futureTasks
                        )
                    }
                }
            }
        }
    }

    private fun setAlarm (notificationTime: Long, futureTasks: List<TaskEntity>) {
        val alarmIntents = mutableListOf<PendingIntent>()
        var requestCode = 0
        futureTasks.forEach { taskEntity ->
            alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
                intent.putExtra("task", taskEntity.task)
                PendingIntent.getBroadcast(context, requestCode, intent, FLAG_IMMUTABLE)
            }
            alarmIntents.add(alarmIntent)
            alarmManager.setExact(
                AlarmManager.RTC,
                taskEntity.taskDate.minusMinutes(notificationTime).getEpoch(),
                alarmIntent)
            requestCode =+ 1
        }
        settingsViewModel.setAlarmIntents(alarmIntents)
    }

    private fun cancelAlarm(alarmIntents: List<PendingIntent>) {
        alarmIntents.forEach { alarmIntent ->
            alarmManager.cancel(alarmIntent)
        }
    }
}