package com.iti.tiempo.ui.alarm

import android.os.Bundle
import android.widget.Toast
import com.iti.tiempo.R
import com.iti.tiempo.base.ui.BaseDialogFragment
import com.iti.tiempo.base.utils.observe
import com.iti.tiempo.base.utils.safeNavigation
import com.iti.tiempo.base.utils.setDateFromTimeStamp
import com.iti.tiempo.base.utils.setTimeForHourFromTimeStamp
import com.iti.tiempo.databinding.DialogFragmentNewAlarmBinding
import com.iti.tiempo.local.AppSharedPreference
import com.iti.tiempo.utils.END_DATE
import com.iti.tiempo.utils.START_DATE
import com.iti.tiempo.utils.TIME
import com.iti.tiempo.utils.TYPE
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AddAlarmDialogFragment :
    BaseDialogFragment<DialogFragmentNewAlarmBinding>(DialogFragmentNewAlarmBinding::inflate) {
    private var startDate: Calendar? = null
    var endDate: Calendar? = null
    var time: Calendar? = null

    @Inject
    lateinit var appSharedPreference: AppSharedPreference
    override fun afterOnViewCreated() {
        super.afterOnViewCreated()
        setOnClickListeners()
        navController.observe<Calendar>(START_DATE, viewLifecycleOwner) { date ->
            if (date != null) {
                startDate = date
                binding.textViewStartDate.setDateFromTimeStamp(date.timeInMillis)
//                navController.currentBackStackEntry?.savedStateHandle?.remove<Calendar>(START_DATE)
            }
        }
        navController.observe<Calendar>(END_DATE, viewLifecycleOwner) { date ->
            if (date != null) {
                endDate = date
                binding.textViewEndDate.setDateFromTimeStamp(date.timeInMillis)
//                navController.currentBackStackEntry?.savedStateHandle?.remove<Calendar>(END_DATE)
            }
        }
        navController.observe<Calendar>(TIME, viewLifecycleOwner) { date ->
            date?.let {
                time = date
                binding.textViewTime.setTimeForHourFromTimeStamp(date.timeInMillis,
                    appSharedPreference,
                    "hh:mm aa")
//                navController.currentBackStackEntry?.savedStateHandle?.remove<Calendar>(TIME)
            }
        }
    }

    private fun setOnClickListeners() {
        binding.textViewStartDate.setOnClickListener {
            endDate=null
            binding.textViewEndDate.text = ""
            navController.safeNavigation(R.id.addAlarmDialogFragment,
                R.id.action_addAlarmDialogFragment_to_datePickerDialogFragment,
                Bundle().apply {
                    putString(TYPE, START_DATE)
                })
        }
        binding.textViewEndDate.setOnClickListener {
            if (startDate != null)

                navController.safeNavigation(R.id.addAlarmDialogFragment,
                    R.id.action_addAlarmDialogFragment_to_datePickerDialogFragment,
                    Bundle().apply {
                        putString(TYPE, END_DATE)
                        putSerializable(START_DATE,startDate)
                    })
            else
                Toast.makeText(context,
                    resources.getString(R.string.start_should_be_before_end),
                    Toast.LENGTH_LONG).show()
        }

        binding.textViewTime.setOnClickListener {
            navController.safeNavigation(R.id.addAlarmDialogFragment,
                R.id.action_addAlarmDialogFragment_to_datePickerDialogFragment,
                Bundle().apply {
                    putString(TYPE, TIME)
                })
        }
    }
}