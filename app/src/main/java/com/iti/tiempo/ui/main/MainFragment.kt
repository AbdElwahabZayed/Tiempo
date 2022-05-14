package com.iti.tiempo.ui.main

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.iti.tiempo.R
import com.iti.tiempo.base.ui.BaseFragment
import com.iti.tiempo.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {
    lateinit var localNavController: NavController

    override fun afterOnViewCreated() {
        super.afterOnViewCreated()
        setUpBottomNavigation()
        setOnDestinationChangedListener()
    }

    private fun setUpBottomNavigation() {
        localNavController =
            (childFragmentManager.findFragmentById(R.id.nav_host_fragment_home) as NavHostFragment).navController
        binding.bottomNavigationView.setupWithNavController(localNavController)
    }

    private fun setOnDestinationChangedListener() {
        localNavController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    binding.textViewTitle.text = resources.getString(R.string.home)
                }
                R.id.favoritesFragment -> {
                    binding.textViewTitle.text = resources.getString(R.string.favorites)

                }
                R.id.settingsFragment -> {
                    binding.textViewTitle.text = resources.getString(R.string.settings)
                }
                R.id.alarmsFragment -> {
                    binding.textViewTitle.text = resources.getString(R.string.alarms)
                }
            }
        }
    }

}