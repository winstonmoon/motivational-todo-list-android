package com.moonwinston.motivationaltodolist.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.moonwinston.motivationaltodolist.BuildConfig
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.databinding.FragmentSettingsBinding
import com.moonwinston.motivationaltodolist.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment: Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.notify.text = resources.getStringArray(R.array.notify_array)[viewModel.notifyIndex.value]
        binding.theme.text = resources.getStringArray(R.array.theme_array)[viewModel.themeIndex.value]
        binding.language.text = resources.getStringArray(R.array.language_array)[viewModel.languageIndex.value]
        binding.versionTextView.text = BuildConfig.VERSION_NAME

        binding.backButton.setOnClickListener {
            it.findNavController().popBackStack()
        }
        binding.notifyButton.setOnClickListener {
            val builder = AlertDialog.Builder(it.context, R.style.CustomAlertDialog)
            val notifyItems = resources.getStringArray(R.array.notify_array)
            val checkedItem = viewModel.notifyIndex.value
            builder.setTitle(resources.getString(R.string.label_notify))
                .setSingleChoiceItems(notifyItems, checkedItem) { dialog, which ->
                    binding.notify.text = notifyItems[which]
                    viewModel.setNotify(which)
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
            val checkedItem = viewModel.themeIndex.value
            builder.setTitle(resources.getString(R.string.label_theme))
                .setSingleChoiceItems(themeItems, checkedItem) { dialog, which ->
                    binding.theme.text = themeItems[which]
                    viewModel.setTheme(which)
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
            val checkedItem = viewModel.languageIndex.value
            builder.setTitle(resources.getString(R.string.label_language))
                .setSingleChoiceItems(languageItems, checkedItem) { dialog, which ->
                    binding.language.text = languageItems[which]
                    viewModel.setLanguage(which)
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.button_cancel) { dialog, _ ->
                    dialog?.cancel()
                }
            builder.show()
        }
    }
}