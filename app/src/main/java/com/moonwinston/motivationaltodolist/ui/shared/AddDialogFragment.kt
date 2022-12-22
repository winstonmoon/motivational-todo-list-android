package com.moonwinston.motivationaltodolist.ui.shared

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.moonwinston.motivationaltodolist.DmlState
import com.moonwinston.motivationaltodolist.MonthEnum
import com.moonwinston.motivationaltodolist.R
import com.moonwinston.motivationaltodolist.data.SharedPref
import com.moonwinston.motivationaltodolist.data.TaskEntity
import com.moonwinston.motivationaltodolist.databinding.DialogAddBinding
import com.moonwinston.motivationaltodolist.utils.CalendarUtil
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class AddDialogFragment : DialogFragment() {
    private lateinit var binding: DialogAddBinding
    private lateinit var date: Date
    private lateinit var dmlState: DmlState
    private lateinit var taskEntity: TaskEntity
    private var positiveButton by Delegates.notNull<Int>()
    private val sharedViewModel by sharedViewModel<SharedViewModel>()
    private val sharedPref: SharedPref by inject()

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
                DmlState.Insert("insert") -> initInsertView(binding, taskEntity)
                DmlState.Insert("copy") -> initCopyView(binding, taskEntity)
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

                        //TODO
//                        val hour = binding.timePicker.hour
//                        val minute = binding.timePicker.minute

                        val time = Calendar.getInstance()
                        time.set(Calendar.HOUR_OF_DAY, binding.timePicker.hour)
                        time.set(Calendar.MINUTE, binding.timePicker.minute)

                        val taskEntity =
                            when (dmlState) {
                                DmlState.Insert("copy") ->
                                    TaskEntity(
                                        taskDate = date,
                                        //TODO
//                                        taskTime = SimpleDateFormat("HH:mm").parse("%02d:%02d".format(hour, minute)),
                                        taskTime = time.time,
                                        task = binding.taskEditText.text.toString(),
                                        isCompleted = false
                                    )
                                else ->
                                    TaskEntity(
                                        uid = taskEntity.uid,
                                        taskDate = date,
                                        //TODO
//                                        taskTime = SimpleDateFormat("HH:mm").parse("%02d:%02d".format(hour, minute)),
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

    override fun onStart() {
        super.onStart()
        //TODO
        (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.main_text))
        (dialog as AlertDialog).getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.main_text))
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
            date  = cal.time
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
//        val year = SimpleDateFormat("y").format(taskEntity.taskTime).toInt()
//        val month = SimpleDateFormat("M").format(taskEntity.taskTime).toInt()
//        val day = SimpleDateFormat("d").format(taskEntity.taskTime).toInt()
//        val hourOfDay = SimpleDateFormat("H").format(taskEntity.taskTime).toInt()
//        val minute = SimpleDateFormat("m").format(taskEntity.taskTime).toInt()
//        val taskEntityTaskTime = GregorianCalendar(year, month, day, hourOfDay, minute)
//        binding.timePicker.hour = taskEntityTaskTime.get(Calendar.HOUR_OF_DAY)
//        binding.timePicker.minute = taskEntityTaskTime.get(Calendar.MINUTE)
        val cal = Calendar.getInstance().apply {
            this.time = taskEntity.taskTime
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
//        val year = SimpleDateFormat("y").format(taskEntity.taskTime).toInt()
//        val month = SimpleDateFormat("M").format(taskEntity.taskTime).toInt()
//        val day = SimpleDateFormat("d").format(taskEntity.taskTime).toInt()
//        val hourOfDay = SimpleDateFormat("H").format(taskEntity.taskTime).toInt()
//        val minute = SimpleDateFormat("m").format(taskEntity.taskTime).toInt()
//        val taskEntityTaskTime = GregorianCalendar(year, month, day, hourOfDay, minute)
//        binding.timePicker.hour = taskEntityTaskTime.get(Calendar.HOUR_OF_DAY)
//        binding.timePicker.minute = taskEntityTaskTime.get(Calendar.MINUTE)
        val cal = Calendar.getInstance().apply {
            this.time = taskEntity.taskTime
        }
        binding.timePicker.hour = cal.get(Calendar.HOUR_OF_DAY)
        binding.timePicker.minute = cal.get(Calendar.MINUTE)
        binding.calendarView.date = taskEntity.taskDate.time
        date = taskEntity.taskDate
        binding.taskEditText.setText(taskEntity.task)
        positiveButton = R.string.button_edit
    }

    private fun createDialogBuilder(
        fragmentActivity: FragmentActivity?,
        binding: DialogAddBinding,
        positiveButton: Int
    ) {
    }
}

