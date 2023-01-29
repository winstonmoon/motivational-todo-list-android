package com.moonwinston.motivationaltodolist.ui.common

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
import com.moonwinston.motivationaltodolist.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@AndroidEntryPoint
class AddDialogFragment : DialogFragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
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
                DmlState.Insert(method = "copy") -> initCopyView(taskEntity)
                DmlState.Update -> initUpdateView(taskEntity)
                else -> Unit
            }
            createDialogBuilder(fragmentActivity = it, positiveButton = addDialogViewModel.positiveButtonStringResource.value)
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun initCommonView() {
        binding.timePicker.setIs24HourView(true)
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            addDialogViewModel.setDate(LocalDate.of(year, month, dayOfMonth).localDateToDate())
        }
    }

    private fun initInsertView(taskEntity: TaskEntity) {
        binding.timePicker.hour
        binding.timePicker.minute
        binding.calendarView.date = taskEntity.taskDate.time
        addDialogViewModel.setDate(taskEntity.taskDate)
        addDialogViewModel.setPositiveButtonStringResource(R.string.button_add)
    }

    private fun initCopyView(taskEntity: TaskEntity) {
        binding.timePicker.hour = taskEntity.taskDate.dateToLocalDateTime().hour
        binding.timePicker.minute = taskEntity.taskDate.dateToLocalDateTime().minute
        binding.calendarView.date = taskEntity.taskDate.time
        addDialogViewModel.setDate(taskEntity.taskDate)
        binding.taskEditText.setText(taskEntity.task)
        addDialogViewModel.setPositiveButtonStringResource(R.string.button_add)
    }

    private fun initUpdateView(taskEntity: TaskEntity) {
        binding.timePicker.hour = taskEntity.taskDate.dateToLocalDateTime().hour
        binding.timePicker.minute = taskEntity.taskDate.dateToLocalDateTime().minute
        binding.calendarView.date = taskEntity.taskDate.time
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
                    //TODO
                    if (addDialogViewModel.date.value.before(dateOfToday())) {
                        Toast.makeText(fragmentActivity, resources.getString(R.string.message_toast_unaddable), Toast.LENGTH_LONG).show()
                        return@OnClickListener
                    }
                    val taskEntity = setTaskEntity()
                    sharedViewModel.insertTask(taskEntity)

                    //TODO
                    val isNotifyOn = sharedViewModel.themeIndex.value != 0
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
        val taskDate = LocalDateTime.of(addDialogViewModel.date.value.dateToLocalDate(), LocalTime.of(hour, minute)).localDateTimeToDate()
        return when (dmlState) {
            DmlState.Insert(method = "copy") ->
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

