package com.moonwinston.motivationaltodolist.ui.shared

import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.databinding.FragmentSettingsBinding
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

        binding.buttonNotify.setOnClickListener {
            val builder = AlertDialog.Builder(it.context)
            val notifyItems = resources.getStringArray(R.array.notify_array)
            builder.setTitle(resources.getString(R.string.label_notify))
                .setSingleChoiceItems(notifyItems, -1,
                    DialogInterface.OnClickListener { dialog, which ->
                        binding.valueNotify.text = notifyItems[which]
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
        binding.buttonTheme.setOnClickListener {
            val builder = AlertDialog.Builder(it.context)
            val themeItems = resources.getStringArray(R.array.theme_array)
            builder.setTitle(resources.getString(R.string.label_theme))
                .setSingleChoiceItems(themeItems, -1,
                    DialogInterface.OnClickListener { dialog, which ->
                        binding.valueTheme.text = themeItems[which]
                        val test = ThemeUtil()
                        test.setTheme(resources.getString(which))
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
            val builder = AlertDialog.Builder(it.context)
            val languageItems = resources.getStringArray(R.array.language_array)
            builder.setTitle(resources.getString(R.string.label_language))
                .setSingleChoiceItems(languageItems, -1,
                    DialogInterface.OnClickListener { dialog, which ->
                        binding.valueLanguage.text = languageItems[which]
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

        binding.buttonBack.setOnClickListener {
            it.findNavController().popBackStack()
        }
    }
}