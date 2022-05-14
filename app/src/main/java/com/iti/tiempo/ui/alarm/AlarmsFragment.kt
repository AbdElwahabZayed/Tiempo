package com.iti.tiempo.ui.alarm

import com.iti.tiempo.base.ui.BaseFragment
import com.iti.tiempo.databinding.FragmentAlarmsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmsFragment : BaseFragment<FragmentAlarmsBinding>(FragmentAlarmsBinding::inflate) {
    override fun afterOnCreateView() {
    }
}