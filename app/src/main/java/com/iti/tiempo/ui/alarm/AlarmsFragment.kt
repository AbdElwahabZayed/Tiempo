package com.iti.tiempo.ui.alarm

import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.iti.tiempo.R
import com.iti.tiempo.base.ui.BaseFragment
import com.iti.tiempo.base.utils.observe
import com.iti.tiempo.base.utils.safeNavigation
import com.iti.tiempo.databinding.FragmentAlarmsBinding
import com.iti.tiempo.ui.alarm.viewmodel.AlarmViewModel
import com.iti.tiempo.utils.END_DATE
import com.iti.tiempo.utils.START_DATE
import com.iti.tiempo.utils.TIME
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AlarmsFragment : BaseFragment<FragmentAlarmsBinding>(FragmentAlarmsBinding::inflate) {
    private val mNavController by lazy {
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }
    private val mViewModel: AlarmViewModel by viewModels()

    override fun afterOnCreateView() {
        mViewModel.getAllAlarms()
        mViewModel.alarms.removeObservers(viewLifecycleOwner)
        mViewModel.alarms.observe(viewLifecycleOwner){
            when(it){
                null -> {}
                else ->{

                }
            }
        }

    }

    override fun afterOnViewCreated() {
        super.afterOnViewCreated()
        binding.fabAddAlarm.setOnClickListener {
            mNavController.safeNavigation(R.id.mainFragment,R.id.action_mainFragment_to_addAlarmDialogFragment)
        }

    }
}