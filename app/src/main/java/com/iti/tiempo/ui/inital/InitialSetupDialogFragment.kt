package com.iti.tiempo.ui.inital

import com.iti.tiempo.R
import com.iti.tiempo.base.ui.BaseDialogFragment
import com.iti.tiempo.base.utils.isGPSActive
import com.iti.tiempo.base.utils.safeNavigation
import com.iti.tiempo.databinding.DialogFragmentInitialSetupBinding
import com.iti.tiempo.local.AppSharedPreference
import com.iti.tiempo.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InitialSetupDialogFragment :
    BaseDialogFragment<DialogFragmentInitialSetupBinding>(DialogFragmentInitialSetupBinding::inflate) {
    @Inject
    lateinit var appSharedPreference: AppSharedPreference
    override fun afterOnViewCreated() {
        super.afterOnViewCreated()
        binding.btnOk.setOnClickListener {
            appSharedPreference.setValue(
                IS_NOTIFICATION_ENABLED,
                binding.switchNotification.isChecked
            )
            appSharedPreference.setValue(
                IS_FIRST,
               false
            )
            when {
                binding.rbGps.isChecked -> {
                    appSharedPreference.setValue(TYPE_OF_LOCATION, GPS)
                    navController.safeNavigation(R.id.initialSetupDialogFragment,R.id.action_initialSetupDialogFragment_to_mainFragment)
                }
                else -> {
                    appSharedPreference.setValue(TYPE_OF_LOCATION, MAP)
                    navController.safeNavigation(R.id.initialSetupDialogFragment,R.id.action_initialSetupDialogFragment_to_placePickerFragment)
                }
            }
        }
    }
}