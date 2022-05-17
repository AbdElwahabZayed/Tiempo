package com.iti.tiempo.ui.alarm

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iti.tiempo.R
import com.iti.tiempo.base.ui.BaseFragment
import com.iti.tiempo.base.utils.observe
import com.iti.tiempo.base.utils.safeNavigation
import com.iti.tiempo.databinding.FragmentAlarmsBinding
import com.iti.tiempo.local.AppSharedPreference
import com.iti.tiempo.model.Alarm
import com.iti.tiempo.ui.alarm.viewmodel.AlarmViewModel
import com.iti.tiempo.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("NotifyDataSetChanged")
@AndroidEntryPoint
class AlarmsFragment : BaseFragment<FragmentAlarmsBinding>(FragmentAlarmsBinding::inflate) {
    private val mNavController by lazy {
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }

    @Inject
    lateinit var appSharedPreference: AppSharedPreference
    private val mViewModel: AlarmViewModel by viewModels()
    private val list: MutableList<Alarm> = mutableListOf()
    private var mAdapter: AlarmsAdapter? = null
    override fun afterOnCreateView() {
        mViewModel.alarms.removeObservers(viewLifecycleOwner)
        mViewModel.alarms.observe(viewLifecycleOwner) {
            when (it) {
                null -> {

                }
                else -> {
                    list.clear()
                    list.addAll(it)
                    mAdapter = AlarmsAdapter(list, appSharedPreference) {
                        mNavController.safeNavigation(R.id.mainFragment,
                            R.id.action_mainFragment_to_deleteFragmentDialog,
                            Bundle().apply {
                                putParcelable(DELETE_ALARM, it)
                            })
                    }
                    binding.rvFavorites.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                        adapter = mAdapter
                    }
                }
            }
        }
        mViewModel.getAllAlarms()
    }

    override fun afterOnViewCreated() {
        super.afterOnViewCreated()
        mNavController.observe<Alarm>(ALARM, viewLifecycleOwner) { alarm ->
            when (alarm) {
                null -> {
                    Toast.makeText(context, "enter alarm again please", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    mViewModel.insertAlarm(alarm)
                    mAdapter?.notifyDataSetChanged()
                }
            }
        }
        mNavController.observe<Alarm>(DELETE_ALARM, viewLifecycleOwner) { alarm ->
            when (alarm) {
                null -> {
                    Toast.makeText(context, "delete alarm again please", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    mViewModel.deleteAlarm(alarm)
                    mAdapter?.notifyDataSetChanged()
                }
            }
        }
        binding.fabAddAlarm.setOnClickListener {
            mNavController.safeNavigation(R.id.mainFragment,
                R.id.action_mainFragment_to_addAlarmDialogFragment)
        }

    }
}