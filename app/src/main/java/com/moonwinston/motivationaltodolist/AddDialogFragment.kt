package com.moonwinston.motivationaltodolist

import android.app.AlertDialog
import android.os.Bundle
import android.app.Dialog
import android.content.DialogInterface
import androidx.fragment.app.DialogFragment

class AddDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            //val inflater = MainActivity().layoutInflater;
            val inflater = this.layoutInflater;

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.dialog_add, null))
                    // Add action buttons
                    .setPositiveButton(R.string.button_add,
                            DialogInterface.OnClickListener { dialog, id ->
                                // sign in the user ...
                            })
                    .setNegativeButton(R.string.button_cancel,
                            DialogInterface.OnClickListener { dialog, id ->
                                getDialog()?.cancel()
                            })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}