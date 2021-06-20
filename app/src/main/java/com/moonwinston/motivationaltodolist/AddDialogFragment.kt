package com.moonwinston.motivationaltodolist

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.moonwinston.motivationaltodolist.databinding.DialogAddBinding
import java.text.SimpleDateFormat
import java.util.*

class AddDialogFragment : DialogFragment() {

    private lateinit var binding: DialogAddBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            binding = DialogAddBinding.inflate(layoutInflater)

            builder.setView(binding.root)
                    .setPositiveButton(R.string.button_add,
                        DialogInterface.OnClickListener { _, _ ->
                            // sign in the user ...
//                            val formatter = SimpleDateFormat("yyyy-MM-dd")
//                            Toast.makeText(context, formatter.format(binding.viewCalendar.date), Toast.LENGTH_LONG).show()

//                            val time = LocalTime.(binding.inputTime.text.toString())
//                            Toast.makeText(context, binding.inputTime.text.toString(), Toast.LENGTH_LONG).show()


                            val formatDate = SimpleDateFormat("yyyy-MM-dd")
                            val dateTest: Date = Date(binding.viewCalendar.date)
                            val stringDateTest = formatDate.format(dateTest)

                            val formatTime = SimpleDateFormat("HH:mm")
                            val timeTest: Date = formatTime.parse(binding.inputTime.text.toString())
                            val stringTimeTest = formatTime.format(timeTest)
                            Toast.makeText(context, stringTimeTest, Toast.LENGTH_LONG).show()

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