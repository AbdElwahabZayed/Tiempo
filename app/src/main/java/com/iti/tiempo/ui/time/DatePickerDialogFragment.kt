package com.iti.tiempo.ui.time

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.iti.tiempo.R

import java.util.*


class DatePickerDialogFragment : DialogFragment() , DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {
    
    companion object{
        const val TAG="DatePickerDialogFrag"
    }
    private lateinit var navController: NavController


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.d(TAG, "onCreateDialog: in com.iti.tiempo.ui.time.DatePickerDialogFragment")
        var tag=arguments?.getString("TYPE")
        if (tag==null)
            tag= "DATE"
        // Use the current date as the default date in the picker

        val c = Calendar.getInstance()
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val hours = c.get(Calendar.HOUR)
        val min = c.get(Calendar.MINUTE)
        val dialog = DatePickerDialog(requireContext(), this,  month, day,hours)
        val timeDialog = TimePickerDialog(requireContext(),this,hours,min,false)
        dialog.datePicker.minDate = System.currentTimeMillis() - 1000

        return if (tag== "DATE"){
            dialog
        }else{

            timeDialog
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        //return with data
        Log.d(TAG, "onDateSet: in")
        val oldDate=arguments?.getSerializable("DATE") as Calendar?
        val date = Calendar.getInstance()
        if (oldDate!=null){
            val hours = oldDate.get(Calendar.HOUR)
            val min = oldDate.get(Calendar.MINUTE)
            date.set(year, month , dayOfMonth, hours, min)
        }else{
            date.set(year, month , dayOfMonth, 0, 0)
        }


        Log.d(TAG, "onDateSet: $date")
        navController.previousBackStackEntry?.savedStateHandle?.set("DATE", date)
        navController.navigateUp()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = findNavController()

    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        //return with data
        Log.d(TAG, "onDateSet: in")
        var date=arguments?.getSerializable("DATE") as Calendar?
        if (date==null) {
            Log.d(TAG, "onTimeSet: null == $date")
            date = Calendar.getInstance()
        }
//        Log.d(TAG, "onTimeSet: before $date")
        val year = date!!.get(Calendar.YEAR)
        val month = date.get(Calendar.MONTH)
        val day = date.get(Calendar.DAY_OF_MONTH)
        date.set(year,month ,day,hourOfDay,  minute)
//        Log.d(TAG, "onTimeSet: after $date")
        if (Calendar.getInstance().timeInMillis>=date.timeInMillis){
            Toast.makeText(context, resources.getText(R.string.expired_date), Toast.LENGTH_SHORT).show()
            navController.previousBackStackEntry?.savedStateHandle?.set("TIME", null)
        }else{
            navController.previousBackStackEntry?.savedStateHandle?.set("TIME", date)
        }
        navController.navigateUp()
    }


}