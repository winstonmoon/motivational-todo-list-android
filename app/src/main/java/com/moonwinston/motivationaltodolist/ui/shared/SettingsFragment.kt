package com.moonwinston.motivationaltodolist.ui.shared

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.moonwinston.motivationaltodolist.BuildConfig
import com.moonwinston.motivationaltodolist.MainActivity
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.SharedPref
import com.moonwinston.motivationaltodolist.databinding.FragmentSettingsBinding
import com.moonwinston.motivationaltodolist.receiver.AlarmReceiver
import com.moonwinston.motivationaltodolist.utils.ThemeUtil
import org.koin.android.ext.android.inject

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val sharedPref: SharedPref by inject()
    private lateinit var alarmManager: AlarmManager
    private lateinit var alarmIntent: PendingIntent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.sharedPreferences = sharedPref
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, 0)
        }

        //TODO use array not text
        binding.notify.text =
            resources.getStringArray(R.array.notify_array)[sharedPref.getNotify()]
        binding.theme.text =
            resources.getStringArray(R.array.theme_array)[sharedPref.getTheme()]
        binding.language.text =
            resources.getStringArray(R.array.language_array)[sharedPref.getLanguage()]

        binding.notifyButton.setOnClickListener {
            val builder = AlertDialog.Builder(it.context, R.style.CustomAlertDialog)
            val notifyItems = resources.getStringArray(R.array.notify_array)
            builder.setTitle(resources.getString(R.string.label_notify))
                .setSingleChoiceItems(notifyItems, -1,
                    DialogInterface.OnClickListener { dialog, which ->
                        binding.notify.text = notifyItems[which]
                        //TODO sharedPreferences
                        //TODO use array not text
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
            //TODO
            val checkedItem = sharedPref.getTheme() ?: -1
            builder.setTitle(resources.getString(R.string.label_theme))
                .setSingleChoiceItems(themeItems, checkedItem,
                    DialogInterface.OnClickListener { dialog, which ->
                        binding.theme.text = themeItems[which]
                        //TODO use array not text
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

        binding.languageButton.setOnClickListener {
            val builder = AlertDialog.Builder(it.context, R.style.CustomAlertDialog)
            val languageItems = resources.getStringArray(R.array.language_array)
            //TODO
            val checkedItem = sharedPref.getLanguage() ?: -1
            builder.setTitle(resources.getString(R.string.label_language))
                .setSingleChoiceItems(languageItems, checkedItem,
                    DialogInterface.OnClickListener { dialog, which ->
                        binding.language.text = languageItems[which]
                        //TODO use array not text
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
}