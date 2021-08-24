package com.moonwinston.motivationaltodolist

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.DialogAddBinding
import com.moonwinston.motivationaltodolist.viewmodels.SharedViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddDialogFragment : DialogFragment() {
    private lateinit var binding: DialogAddBinding
    private lateinit var date: Date

    private lateinit var dmlState: DmlState
    private lateinit var taskEntity: TaskEntity

    private val formatDate = SimpleDateFormat("yyyy-MM-dd")

    private var isDateChanged = false

    private val sharedViewModel by sharedViewModel<SharedViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dmlState = arguments?.getParcelable<DmlState>("dmlState") as DmlState
        taskEntity = arguments?.getParcelable<TaskEntity>("taskEntity") as TaskEntity
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
//            when (dmlState) {
//                DmlState.Insert -> insert(it)
//
//
//                DmlState.Update -> update(it, taskEntity)
//
//            }
            //TODO fix
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
            if (dmlState.equals(DmlState.Update)) {
                binding.inputTask.setText(taskEntity.task)
            }

            builder.setView(binding.root)
                .setPositiveButton(if (dmlState.equals(DmlState.Insert)) R.string.button_add else R.string.button_edit,
                    DialogInterface.OnClickListener { _, _ ->
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

                        var insertTaskEntity = TaskEntity(
                            uid = taskEntity?.uid,
                            taskDate = date,
                            taskTime = time,
                            task = task,
                            isGoalSet = isGoadlSet,
                            isCompleted = false
                        )
                        sharedViewModel.insert(insertTaskEntity)
                    })
                .setNegativeButton(R.string.button_cancel,
                    DialogInterface.OnClickListener { _, _ ->
                        dialog?.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun insert(fragmentActivity: FragmentActivity) {
        if (fragmentActivity == null) {
            throw IllegalStateException("Activity cannot be null")
        }
        val builder = AlertDialog.Builder(fragmentActivity, R.style.CustomAlertDialog)
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
    }

    private fun update(fragmentActivity: FragmentActivity, taskEntity: TaskEntity) {
        if (fragmentActivity == null) {
            throw IllegalStateException("Activity cannot be null")
        }
        val builder = AlertDialog.Builder(fragmentActivity, R.style.CustomAlertDialog)
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
            .setPositiveButton("EDIT",
                DialogInterface.OnClickListener { _, _ ->
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
    }
}

