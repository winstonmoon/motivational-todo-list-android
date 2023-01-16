package com.moonwinston.motivationaltodolist.ui.shared

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.telecom.Call
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.moonwinston.motivationaltodolist.DmlState
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.DialogAddBinding
import com.moonwinston.motivationaltodolist.utils.CalendarUtil
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.properties.Delegates

@AndroidEntryPoint
class AddDialogFragment : DialogFragment() {
    private lateinit var binding: DialogAddBinding
    private lateinit var date: Date
    private lateinit var dmlState: DmlState
    private lateinit var taskEntity: TaskEntity
    private var positiveButton by Delegates.notNull<Int>()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dmlState = arguments?.getParcelable<DmlState>("dmlState") as DmlState
        taskEntity = arguments?.getParcelable<TaskEntity>("taskEntity") as TaskEntity
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            binding = DialogAddBinding.inflate(layoutInflater)
            initCommonView(binding)
            when (dmlState) {
                DmlState.Insert(method = "insert") -> initInsertView(binding, taskEntity)
                DmlState.Insert(method = "copy") -> initCopyView(binding, taskEntity)
                DmlState.Update -> initUpdateView(binding, taskEntity)
                else -> Unit
            }

            val builder = AlertDialog.Builder(it, R.style.CustomAlertDialog)
            builder.setView(binding.root)
                .setPositiveButton(positiveButton,
                    DialogInterface.OnClickListener { _, _ ->
                        //TODO
                        if (date.before(CalendarUtil.getTodayDate())) {
                            Toast.makeText(it, resources.getString(R.string.message_toast_unaddable), Toast.LENGTH_LONG).show()
                            return@OnClickListener
                        }

                        val cal = Calendar.getInstance()
                        cal.time = date

                        val taskDate = Calendar.getInstance().apply {
                            set(Calendar.YEAR, cal.get(Calendar.YEAR))
                            set(Calendar.MONTH, cal.get(Calendar.MONTH))
                            set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH))
                            set(Calendar.HOUR_OF_DAY, binding.timePicker.hour)
                            set(Calendar.MINUTE, binding.timePicker.minute)
                        }

                        val time = Calendar.getInstance()
                        time.set(Calendar.HOUR_OF_DAY, binding.timePicker.hour)
                        time.set(Calendar.MINUTE, binding.timePicker.minute)

                        val taskEntity =
                            when (dmlState) {
                                DmlState.Insert(method = "copy") ->
                                    TaskEntity(
                                        taskDate = date,
                                        taskTime = time.time,
                                        task = binding.taskEditText.text.toString(),
                                        isCompleted = false
                                    )
                                else ->
                                    TaskEntity(
                                        uid = taskEntity.uid,
                                        taskDate = date,
                                        taskTime = time.time,
                                        task = binding.taskEditText.text.toString(),
                                        isCompleted = false
                                    )
                            }
                        sharedViewModel.insertTask(taskEntity)

                        //TODO
//                        val isNotifySet = sharedPref.getNotify() != 0
//                        if (isNotifySet) {
//                            alarmmanager
//                            pendingintent
//                        }
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
        binding.timePicker.setIs24HourView(true)
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            //TODO
//            val parsedMonth = resources.getString(MonthEnum.values()[month].monthNumber)
//            date = SimpleDateFormat("yyyy-MM-dd").parse("$year-$parsedMonth-$dayOfMonth")
            val cal = Calendar.getInstance()
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month + 1)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            date = cal.time
        }
    }

    private fun initInsertView(binding: DialogAddBinding, taskEntity: TaskEntity) {
        binding.timePicker.hour
        binding.timePicker.minute
        binding.calendarView.date = taskEntity.taskDate.time
        date = taskEntity.taskDate
        positiveButton = R.string.button_add
    }

    private fun initCopyView(binding: DialogAddBinding, taskEntity: TaskEntity) {
        //TODO
        val cal = Calendar.getInstance().apply {
            time = taskEntity.taskTime
        }
        binding.timePicker.hour = cal.get(Calendar.HOUR_OF_DAY)
        binding.timePicker.minute = cal.get(Calendar.MINUTE)
        binding.calendarView.date = taskEntity.taskDate.time
        date = taskEntity.taskDate
        binding.taskEditText.setText(taskEntity.task)
        positiveButton = R.string.button_add
    }

    private fun initUpdateView(binding: DialogAddBinding, taskEntity: TaskEntity) {
        //TODO
        val cal = Calendar.getInstance().apply {
            time = taskEntity.taskTime
        }
        binding.timePicker.hour = cal.get(Calendar.HOUR_OF_DAY)
        binding.timePicker.minute = cal.get(Calendar.MINUTE)
        binding.calendarView.date = taskEntity.taskDate.time
        date = taskEntity.taskDate
        binding.taskEditText.setText(taskEntity.task)
        positiveButton = R.string.button_edit
    }

    override fun onStart() {
        super.onStart()
        //TODO
        (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.main_text))
        (dialog as AlertDialog).getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.main_text))
    }

    private fun createDialogBuilder(
        fragmentActivity: FragmentActivity?,
        binding: DialogAddBinding,
        positiveButton: Int
    ) {
    }
}

