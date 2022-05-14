package com.iti.tiempo.ui.splash

import androidx.lifecycle.lifecycleScope
import com.iti.tiempo.R
import com.iti.tiempo.base.ui.BaseFragment
import com.iti.tiempo.base.utils.safeNavigation
import com.iti.tiempo.databinding.FragmentSplashBinding
import com.iti.tiempo.local.AppSharedPreference
import com.iti.tiempo.utils.IS_FIRST
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {
    @Inject
    lateinit var appSharedPreference: AppSharedPreference
    override fun afterOnViewCreated() {
        super.afterOnViewCreated()
        lifecycleScope.launch(Dispatchers.Default) {
            delay(1000)
            withContext(Dispatchers.Main) {
                if (appSharedPreference.getBooleanValue(IS_FIRST, true)) {
                    navController.safeNavigation(
                        R.id.splashFragment,
                        R.id.action_splashFragment_to_initialSetupDialogFragment
                    )
                } else {

                    navController.safeNavigation(
                        R.id.splashFragment,
                        R.id.action_splashFragment_to_mainFragment
                    )
                }
            }
        }
    }
}