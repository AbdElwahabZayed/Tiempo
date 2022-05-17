package com.iti.tiempo.ui.time

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.iti.tiempo.R
import com.iti.tiempo.utils.END_DATE
import com.iti.tiempo.utils.START_DATE
import com.iti.tiempo.utils.TIME
import com.iti.tiempo.utils.TYPE

import java.util.*


class DatePickerDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    companion object {
        const val TAG = "DatePickerDialogFrag"
    }

    private lateinit var navController: NavController


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.d(TAG, "onCreateDialog: in com.iti.tiempo.ui.time.DatePickerDialogFragment")
        val tag = arguments?.getString(TYPE)


        val c = Calendar.getInstance()
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val hours = c.get(Calendar.HOUR)
        val min = c.get(Calendar.MINUTE)


        return when (tag) {
            START_DATE -> {
                DatePickerDialog(requireContext(), this, month, day, hours).apply {
                    this.datePicker.minDate = System.currentTimeMillis() - 1000
                }
            }
            END_DATE -> {
                val startDate = arguments?.get(START_DATE) as? Calendar
                DatePickerDialog(requireContext(), this, month, day, hours).apply {
                    this.datePicker.minDate =
                        startDate?.timeInMillis ?: System.currentTimeMillis() - 1000
                }
            }
            else -> {
                TimePickerDialog(requireContext(), this, hours, min, false)
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        //return with data
        Log.d(TAG, "onDateSet: in")
        val startDate = arguments?.getSerializable(START_DATE) as Calendar?
        val date = Calendar.getInstance()



        date.set(year, month, dayOfMonth, 0, 0)

        Log.d(TAG, "onDateSet: $date")
        when {
            startDate == null -> {
                date.set(year, month, dayOfMonth, 0, 0)
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    START_DATE,
                    date)
            }
            startDate.after(date) -> Toast.makeText(context,
                "sssssssssssss",
                Toast.LENGTH_LONG).show()
            else -> {
                date.set(year, month, dayOfMonth, 23, 59)

                navController.previousBackStackEntry?.savedStateHandle?.set(END_DATE, date)
            }
        }

        navController.navigateUp()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = findNavController()
    }


    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        //return with data
        Log.d(TAG, "onDateSet: in")
        val date = Calendar.getInstance()

        val year = date.get(Calendar.YEAR)
        val month = date.get(Calendar.MONTH)
        val day = date.get(Calendar.DAY_OF_MONTH)
        date.set(year, month, day, hourOfDay, minute)
        navController.previousBackStackEntry?.savedStateHandle?.set(TIME, date)

        navController.navigateUp()
    }


}