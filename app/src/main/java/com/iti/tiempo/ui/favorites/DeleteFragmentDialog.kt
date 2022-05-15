package com.iti.tiempo.ui.favorites

import com.iti.tiempo.base.ui.BaseDialogFragment
import com.iti.tiempo.databinding.DialogFragmentDeleteBinding
import com.iti.tiempo.utils.DELETE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteFragmentDialog :
    BaseDialogFragment<DialogFragmentDeleteBinding>(DialogFragmentDeleteBinding::inflate) {
    override fun afterOnViewCreated() {
        super.afterOnViewCreated()
        binding.btnOk.setOnClickListener {
            navController.previousBackStackEntry?.savedStateHandle?.set(DELETE,arguments?.get(DELETE))
            navController.navigateUp()
        }
        isCancelable = true
    }
}