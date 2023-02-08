package com.moonwinston.motivationaltodolist.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.moonwinston.motivationaltodolist.DmlState
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.DialogAddBinding
import com.moonwinston.motivationaltodolist.ui.main.MainViewModel
import com.moonwinston.motivationaltodolist.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset

@AndroidEntryPoint
class AddDialogFragment : DialogFragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private val addDialogViewModel: AddDialogViewModel by viewModels()
    private lateinit var binding: DialogAddBinding
    private lateinit var dmlState: DmlState
    private lateinit var taskEntity: TaskEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dmlState = arguments?.getParcelable<DmlState>("dmlState") as DmlState
        taskEntity = arguments?.getParcelable<TaskEntity>("taskEntity") as TaskEntity
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            binding = DialogAddBinding.inflate(layoutInflater)
            initCommonView()
            when (dmlState) {
                DmlState.Insert(method = "insert") -> initInsertView(taskEntity)
                DmlState.Insert(method = "duplicate") -> initDuplicateView(taskEntity)
                DmlState.Update -> initUpdateView(taskEntity)
                else -> Unit
            }
            createDialogBuilder(fragmentActivity = it, positiveButton = addDialogViewModel.positiveButtonStringResource.value)
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun initCommonView() {
        binding.timePicker.setIs24HourView(true)
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            addDialogViewModel.setDate(OffsetDateTime.of(year, month, dayOfMonth,0,0,0,0,ZoneOffset.UTC))
        }
    }

    private fun initInsertView(taskEntity: TaskEntity) {
        binding.timePicker.hour
        binding.timePicker.minute
        binding.calendarView.date = taskEntity.taskDate.toEpochSecond()
        addDialogViewModel.setDate(taskEntity.taskDate)
        addDialogViewModel.setPositiveButtonStringResource(R.string.button_add)
    }

    private fun initDuplicateView(taskEntity: TaskEntity) {
        binding.timePicker.hour = taskEntity.taskDate.hour
        binding.timePicker.minute = taskEntity.taskDate.minute
        binding.calendarView.date = taskEntity.taskDate.toEpochSecond()
        addDialogViewModel.setDate(taskEntity.taskDate)
        binding.taskEditText.setText(taskEntity.task)
        addDialogViewModel.setPositiveButtonStringResource(R.string.button_add)
    }

    private fun initUpdateView(taskEntity: TaskEntity) {
        binding.timePicker.hour = taskEntity.taskDate.hour
        binding.timePicker.minute = taskEntity.taskDate.minute
        binding.calendarView.date = taskEntity.taskDate.toEpochSecond()
        addDialogViewModel.setDate(taskEntity.taskDate)
        binding.taskEditText.setText(taskEntity.task)
        addDialogViewModel.setPositiveButtonStringResource(R.string.button_edit)
    }

    private fun createDialogBuilder(
        fragmentActivity: FragmentActivity,
        positiveButton: Int
    ): Dialog {
        val builder = AlertDialog.Builder(fragmentActivity, R.style.CustomAlertDialog)
        builder.setView(binding.root)
            .setPositiveButton(positiveButton,
                DialogInterface.OnClickListener { _, _ ->
                    if (addDialogViewModel.date.value.isBefore(dateOfToday())) {
                        Toast.makeText(fragmentActivity, resources.getString(R.string.message_toast_unaddable), Toast.LENGTH_LONG).show()
                        return@OnClickListener
                    }
                    val taskEntity = setTaskEntity()
                    mainViewModel.insertTask(taskEntity)

                    //TODO
                    val isNotifyOn = mainViewModel.themeIndex.value != 0
                    if (isNotifyOn) {
//                            alarmmanager
//                            pendingintent
                    }
                })
            .setNegativeButton(R.string.button_cancel) { _, _ ->
                dialog?.cancel()
            }
        return builder.create()
    }

    private fun setTaskEntity(): TaskEntity {
        val hour = binding.timePicker.hour
        val minute = binding.timePicker.minute
        val taskDate = OffsetDateTime.of(addDialogViewModel.date.value.toLocalDate(), LocalTime.of(hour, minute), ZoneOffset.UTC)
        return when (dmlState) {
            DmlState.Insert(method = "duplicate") ->
                TaskEntity(
                    taskDate = taskDate,
                    task = binding.taskEditText.text.toString(),
                    isCompleted = false
                )
            else ->
                TaskEntity(
                    uid = taskEntity.uid,
                    taskDate = taskDate,
                    task = binding.taskEditText.text.toString(),
                    isCompleted = false
                )
        }
    }

    override fun onStart() {
        super.onStart()
        (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.main_text))
        (dialog as AlertDialog).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.main_text))
    }
}

