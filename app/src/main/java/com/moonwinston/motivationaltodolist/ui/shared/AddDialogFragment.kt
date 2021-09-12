package com.moonwinston.motivationaltodolist.ui.shared

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.moonwinston.motivationaltodolist.DmlState
import com.moonwinston.motivationaltodolist.MonthEnum
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.DialogAddBinding
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddDialogFragment : DialogFragment() {
    private lateinit var binding: DialogAddBinding
    private lateinit var date: Date
    private lateinit var dmlState: DmlState
    private lateinit var taskEntity: TaskEntity
    private val sharedViewModel by sharedViewModel<SharedViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dmlState = arguments?.getParcelable<DmlState>("dmlState") as DmlState
        taskEntity = arguments?.getParcelable<TaskEntity>("taskEntity") as TaskEntity
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            binding = DialogAddBinding.inflate(layoutInflater)
            var positiveButton: Int = R.string.button_add

            initCommonView(binding)
            when (dmlState) {
                DmlState.Insert -> {
                    initInsertView(binding, taskEntity)
                }
                DmlState.Update -> {
                    initUpdateView(binding, taskEntity)
                    positiveButton = R.string.button_edit
                }
                else -> Unit
            }

            val builder = AlertDialog.Builder(it, R.style.CustomAlertDialog)
            builder.setView(binding.root)
                .setPositiveButton(positiveButton,
                    DialogInterface.OnClickListener { _, _ ->
                        val hour =
                            if (Build.VERSION.SDK_INT <= 23) binding.inputTime.currentHour else binding.inputTime.hour

                        val minute =
                            if (Build.VERSION.SDK_INT <= 23) binding.inputTime.currentMinute else binding.inputTime.minute

                        var taskEntity = TaskEntity(
                            uid = taskEntity?.uid,
                            taskDate = date,
                            taskTime = SimpleDateFormat("HH:mm").parse("%02d:%02d".format(hour, minute)),
                            task = binding.inputTask.text.toString(),
                            isCompleted = false
                        )
                        sharedViewModel.insert(taskEntity)
                        //TODO
//                        sharedViewModel.getAll()
                    })
                .setNegativeButton(
                    R.string.button_cancel,
                    DialogInterface.OnClickListener { _, _ ->
                        dialog?.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun initCommonView(binding: DialogAddBinding) {
        binding.inputTime.setIs24HourView(true)
        binding.viewCalendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val parsedMonth = resources.getString(MonthEnum.values()[month].monthNumber)
            date = SimpleDateFormat("yyyy-MM-dd").parse("$year-$parsedMonth-$dayOfMonth")
        }
    }

    private fun initInsertView(binding: DialogAddBinding, taskEntity: TaskEntity) {
        if (Build.VERSION.SDK_INT <= 23) {
            binding.inputTime.currentHour
            binding.inputTime.currentMinute
        } else {
            binding.inputTime.hour
            binding.inputTime.minute
        }
        binding.viewCalendar.date = taskEntity.taskDate.time
        date = taskEntity.taskDate
    }

    private fun initUpdateView(binding: DialogAddBinding, taskEntity: TaskEntity) {
        if (Build.VERSION.SDK_INT <= 23) {
            binding.inputTime.currentHour = taskEntity.taskTime.hours
            binding.inputTime.currentMinute = taskEntity.taskTime.minutes
        } else {
            binding.inputTime.hour = taskEntity.taskTime.hours
            binding.inputTime.minute = taskEntity.taskTime.minutes
        }
        binding.viewCalendar.date = taskEntity.taskDate.time
        date = taskEntity.taskDate
        binding.inputTask.setText(taskEntity.task)
    }

    private fun createDialogBuilder(fragmentActivity: FragmentActivity?, binding: DialogAddBinding, positiveButton: Int) {

    }
}

