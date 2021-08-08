package com.moonwinston.motivationaltodolist

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.CalendarView
import androidx.fragment.app.DialogFragment
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.DialogAddBinding
import com.moonwinston.motivationaltodolist.viewmodels.SharedViewModel
import org.koin.android.ext.android.bind
import java.text.SimpleDateFormat
import java.util.*
import org.koin.android.viewmodel.ext.android.viewModel

class AddDialogFragment : DialogFragment() {
    private lateinit var binding: DialogAddBinding
    private lateinit var date: Date

    private val formatDate = SimpleDateFormat("yyyy-MM-dd")

    private var isDateChanged = false

    private val sharedViewModel: SharedViewModel by viewModel()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it, R.style.CustomAlertDialog)
            binding = DialogAddBinding.inflate(layoutInflater)
            binding.inputTime.setIs24HourView(true)
            if (Build.VERSION.SDK_INT <= 23) {
                binding.inputTime.currentHour
                binding.inputTime.currentMinute
            } else {
                binding.inputTime.hour
                binding.inputTime.minute
            }
            binding.viewCalendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
                val parsedMonth = resources.getString(MonthEnum.values()[month].monthNumber)
                date = formatDate.parse("$year-$parsedMonth-$dayOfMonth")
                isDateChanged = true
            }

            builder.setView(binding.root)
                .setPositiveButton(R.string.button_add,
                    DialogInterface.OnClickListener { _, _ ->
//                        date = Date(binding.viewCalendar.date)

                        if (isDateChanged.not()) {
                            val todayString = formatDate.format(Date(binding.viewCalendar.date))
                            date = formatDate.parse(todayString)
                        }

                        val hour by lazy {
                            if (Build.VERSION.SDK_INT <= 23) binding.inputTime.currentHour else binding.inputTime.hour
                        }
                        val minute by lazy {
                            if (Build.VERSION.SDK_INT <= 23) binding.inputTime.currentMinute else binding.inputTime.minute
                        }
                        val formatTime = SimpleDateFormat("HH:mm")
                        val time = formatTime.parse("%02d:%02d".format(hour, minute))

                        val task = binding.inputTask.text.toString()

                        val isGoadlSet = binding.switchGoalTask.isChecked

                        val taskEntity = TaskEntity(
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
    }
}