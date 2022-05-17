package com.iti.tiempo.ui.favorites

import com.iti.tiempo.base.ui.BaseDialogFragment
import com.iti.tiempo.databinding.DialogFragmentDeleteBinding
import com.iti.tiempo.utils.DELETE
import com.iti.tiempo.utils.DELETE_ALARM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteFragmentDialog :
    BaseDialogFragment<DialogFragmentDeleteBinding>(DialogFragmentDeleteBinding::inflate) {
    override fun afterOnViewCreated() {
        super.afterOnViewCreated()
        binding.btnOk.setOnClickListener {
            if (arguments?.containsKey(DELETE) == true)
                navController.previousBackStackEntry?.savedStateHandle?.set(DELETE,
                    arguments?.get(DELETE))
            else
                navController.previousBackStackEntry?.savedStateHandle?.set(DELETE_ALARM,
                    arguments?.get(DELETE_ALARM))
            navController.navigateUp()
        }
        isCancelable = true
    }
}