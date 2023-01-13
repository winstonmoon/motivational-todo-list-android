package com.moonwinston.motivationaltodolist.ui.settings

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.moonwinston.motivationaltodolist.MainActivity
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.databinding.FragmentSettingsBinding
import com.moonwinston.motivationaltodolist.receiver.AlarmReceiver
import com.moonwinston.motivationaltodolist.ui.shared.SharedViewModel
import com.moonwinston.motivationaltodolist.utils.ThemeUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    private val settingsViewModel: SettingsViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
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
        binding.lifecycleOwner = this@SettingsFragment
//        binding.sharedPreferences = sharedPref

        alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, FLAG_IMMUTABLE,)
        }

//        binding.notify.text = resources.getStringArray(R.array.notify_array)[sharedPref.getNotify()]
//        binding.theme.text = resources.getStringArray(R.array.theme_array)[sharedPref.getTheme()]
//        binding.language.text = resources.getStringArray(R.array.language_array)[sharedPref.getLanguage()]

//        sharedViewModel.getNotify()
//        sharedViewModel.getTheme()
//        sharedViewModel.getLanguage()
        binding.notify.text = resources.getStringArray(R.array.notify_array)[sharedViewModel.notifyIndex.value]
        binding.theme.text = resources.getStringArray(R.array.theme_array)[sharedViewModel.themeIndex.value]
        binding.language.text = resources.getStringArray(R.array.language_array)[sharedViewModel.languageIndex.value]

        binding.notifyButton.setOnClickListener {
            val builder = AlertDialog.Builder(it.context, R.style.CustomAlertDialog)
            val notifyItems = resources.getStringArray(R.array.notify_array)
//            sharedViewModel.getNotify()
            val checkedItem = sharedViewModel.notifyIndex.value
            builder.setTitle(resources.getString(R.string.label_notify))
                .setSingleChoiceItems(notifyItems, checkedItem,
                    DialogInterface.OnClickListener { dialog, which ->
                        binding.notify.text = notifyItems[which]
                        sharedViewModel.setNotify(which)
                        //TODO
                        //setAlarm()
                        //TODO sharedPreferences
                        dialog.dismiss()
                    })
                .setNegativeButton(
                    R.string.button_cancel,
                    DialogInterface.OnClickListener { dialog, _ ->
                        dialog?.cancel()
                    })
            builder.show()
        }
        binding.themeButton.setOnClickListener {
            val builder = AlertDialog.Builder(it.context, R.style.CustomAlertDialog)
            val themeItems = resources.getStringArray(R.array.theme_array)
//            sharedViewModel.getTheme()
            val checkedItem = sharedViewModel.themeIndex.value
            builder.setTitle(resources.getString(R.string.label_theme))
                .setSingleChoiceItems(themeItems, checkedItem,
                    DialogInterface.OnClickListener { dialog, which ->
                        binding.theme.text = themeItems[which]
                        sharedViewModel.setTheme(which)
                        ThemeUtil().setTheme(which)
                        dialog.dismiss()
                    })
                .setNegativeButton(
                    R.string.button_cancel,
                    DialogInterface.OnClickListener { dialog, _ ->
                        dialog?.cancel()
                    })
            builder.show()
        }

        binding.languageButton.setOnClickListener {
            val builder = AlertDialog.Builder(it.context, R.style.CustomAlertDialog)
            val languageItems = resources.getStringArray(R.array.language_array)
//            sharedViewModel.getLanguage()
            val checkedItem = sharedViewModel.languageIndex.value
            builder.setTitle(resources.getString(R.string.label_language))
                .setSingleChoiceItems(languageItems, checkedItem,
                    DialogInterface.OnClickListener { dialog, which ->
                        binding.language.text = languageItems[which]
                        sharedViewModel.setLanguage(which)
                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                        dialog.dismiss()
                    })
                .setNegativeButton(
                    R.string.button_cancel,
                    DialogInterface.OnClickListener { dialog, _ ->
                        dialog?.cancel()
                    })
            builder.show()
        }
    }

    private fun setAlarm (notifyTime: Int) {

    }
}