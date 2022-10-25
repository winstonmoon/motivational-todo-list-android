package com.moonwinston.motivationaltodolist.ui.settings

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.moonwinston.motivationaltodolist.MainActivity
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.SharedPref
import com.moonwinston.motivationaltodolist.databinding.FragmentSettingsBinding
import com.moonwinston.motivationaltodolist.receiver.AlarmReceiver
import com.moonwinston.motivationaltodolist.ui.base.BaseFragment
import com.moonwinston.motivationaltodolist.utils.ThemeUtil
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment  : BaseFragment<FragmentSettingsBinding>() {
    override fun getViewBinding() = FragmentSettingsBinding.inflate(layoutInflater)
    private val viewModel by viewModel<SettingsViewModel>()
    private val sharedPref: SharedPref by inject()
    private lateinit var alarmManager: AlarmManager
    private lateinit var alarmIntent: PendingIntent

    override fun initViews() = with(binding) {

        lifecycleOwner = this@SettingsFragment
        sharedPreferences = sharedPref

        alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, FLAG_IMMUTABLE,)
        }

        notify.text = resources.getStringArray(R.array.notify_array)[sharedPref.getNotify()]
        theme.text = resources.getStringArray(R.array.theme_array)[sharedPref.getTheme()]
        language.text = resources.getStringArray(R.array.language_array)[sharedPref.getLanguage()]

        notifyButton.setOnClickListener {
            val builder = AlertDialog.Builder(it.context, R.style.CustomAlertDialog)
            val notifyItems = resources.getStringArray(R.array.notify_array)
            val checkedItem = sharedPref.getNotify()
            builder.setTitle(resources.getString(R.string.label_notify))
                .setSingleChoiceItems(notifyItems, checkedItem,
                    DialogInterface.OnClickListener { dialog, which ->
                        notify.text = notifyItems[which]
                        sharedPref.saveNotify(which)
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
        themeButton.setOnClickListener {
            val builder = AlertDialog.Builder(it.context, R.style.CustomAlertDialog)
            val themeItems = resources.getStringArray(R.array.theme_array)
            val checkedItem = sharedPref.getTheme()
            builder.setTitle(resources.getString(R.string.label_theme))
                .setSingleChoiceItems(themeItems, checkedItem,
                    DialogInterface.OnClickListener { dialog, which ->
                        theme.text = themeItems[which]
                        sharedPref.saveTheme(which)
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

        languageButton.setOnClickListener {
            val builder = AlertDialog.Builder(it.context, R.style.CustomAlertDialog)
            val languageItems = resources.getStringArray(R.array.language_array)
            val checkedItem = sharedPref.getLanguage()
            builder.setTitle(resources.getString(R.string.label_language))
                .setSingleChoiceItems(languageItems, checkedItem,
                    DialogInterface.OnClickListener { dialog, which ->
                        language.text = languageItems[which]
                        sharedPref.saveLanguage(which)
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

    override fun observeData() {

    }

    private fun setAlarm (notifyTime: Int) {

    }
}