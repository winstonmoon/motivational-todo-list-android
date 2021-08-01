package com.moonwinston.motivationaltodolist

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.moonwinston.motivationaltodolist.data.entity.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.DialogAddBinding
import com.moonwinston.motivationaltodolist.viewmodels.SharedViewModel
import java.text.SimpleDateFormat
import java.util.*
import org.koin.android.viewmodel.ext.android.viewModel

class AddDialogFragment : DialogFragment() {

    private lateinit var binding: DialogAddBinding

    private val sharedViewModel: SharedViewModel by viewModel()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.CustomAlertDialog)
            binding = DialogAddBinding.inflate(layoutInflater)

            builder.setView(binding.root)
                .setPositiveButton(R.string.button_add,
                    DialogInterface.OnClickListener { _, _ ->
//                        val formatDate = SimpleDateFormat("yyyy-MM-dd")
//                        val date: Date = formatDate.parse(binding.viewCalendar.date.toString())
                        val date = Date(binding.viewCalendar.date)

                        val formatTime = SimpleDateFormat("HH:mm")
                        val time = formatTime.parse(binding.inputTime.text.toString())

                        val task = binding.inputTask.toString()

                        val isGoadlSet = binding.switchGoalTask.isChecked

                        val taskEntity = TaskEntity(
                            uid = 1,
                            taskDate = date,
                            taskTime = time,
                            task = task,
                            isGoalSet = isGoadlSet,
                            isCompleted = false
                        )
                        sharedViewModel.insert(taskEntity)
                    })
                .setNegativeButton(R.string.button_cancel,
                    DialogInterface.OnClickListener { _, _ ->
                        dialog?.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }
}