package com.moonwinston.motivationaltodolist.ui.shared

import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.moonwinston.motivationaltodolist.MainActivity
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.SharedManager
import com.moonwinston.motivationaltodolist.databinding.FragmentSettingsBinding
import com.moonwinston.motivationaltodolist.utils.LanguageUtil
import com.moonwinston.motivationaltodolist.utils.ThemeUtil

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

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

        //TODO use array not text
        //binding.valueNotify.text = SharedManager(view.context).getNotify().toString()
        binding.valueTheme.text = SharedManager(view.context).getTheme()?:"Follow system"
        binding.valueLanguage.text = SharedManager(view.context).getLanguage()?:"Follow system"

        binding.buttonNotify.setOnClickListener {
            val builder = AlertDialog.Builder(it.context, R.style.CustomAlertDialog)
            val notifyItems = resources.getStringArray(R.array.notify_array)
            builder.setTitle(resources.getString(R.string.label_notify))
                .setSingleChoiceItems(notifyItems, -1,
                    DialogInterface.OnClickListener { dialog, which ->
                        binding.valueNotify.text = notifyItems[which]
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
        binding.buttonTheme.setOnClickListener {
            val builder = AlertDialog.Builder(it.context, R.style.CustomAlertDialog)
            val themeItems = resources.getStringArray(R.array.theme_array)
            //TODO
            val checkedItem = themeItems.indexOf(SharedManager(view.context).getTheme()?:"Follow system")?:-1
            builder.setTitle(resources.getString(R.string.label_theme))
                .setSingleChoiceItems(themeItems, checkedItem,
                    DialogInterface.OnClickListener { dialog, which ->
                        binding.valueTheme.text = themeItems[which]
                        //TODO use array not text
                        SharedManager(view.context).saveTheme(themeItems[which])
                        ThemeUtil().setTheme(themeItems[which])
                        dialog.dismiss()
                    })
                .setNegativeButton(
                    R.string.button_cancel,
                    DialogInterface.OnClickListener { dialog, _ ->
                        dialog?.cancel()
                    })
            builder.show()
        }

        binding.buttonLanguage.setOnClickListener {
            val builder = AlertDialog.Builder(it.context, R.style.CustomAlertDialog)
            val languageItems = resources.getStringArray(R.array.language_array)
            //TODO
            val checkedItem = languageItems.indexOf(SharedManager(view.context).getLanguage()?:"Follow system")?:-1
            builder.setTitle(resources.getString(R.string.label_language))
                .setSingleChoiceItems(languageItems, checkedItem,
                    DialogInterface.OnClickListener { dialog, which ->
                        binding.valueLanguage.text = languageItems[which]
                        //TODO use array not text
                        SharedManager(view.context).saveLanguage(languageItems[which])
                        LanguageUtil().setLanguague(languageItems[which])
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

        binding.buttonBack.setOnClickListener {
            it.findNavController().popBackStack()
        }
    }
}