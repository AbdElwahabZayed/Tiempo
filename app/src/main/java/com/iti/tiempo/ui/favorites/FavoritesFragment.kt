package com.iti.tiempo.ui.favorites


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.iti.tiempo.R
import com.iti.tiempo.base.ui.BaseFragment
import com.iti.tiempo.base.utils.safeNavigation
import com.iti.tiempo.databinding.FragmentFavoriteBinding
import com.iti.tiempo.local.AppSharedPreference
import com.iti.tiempo.model.LocationDetails
import com.iti.tiempo.ui.favorites.viewmodel.FavoritesViewModel
import com.iti.tiempo.ui.home.viewmodel.HomeViewModel
import com.iti.tiempo.ui.placepicker.PlacePickerFragment.Companion.LOCATION_KEY
import com.iti.tiempo.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.lang.IllegalArgumentException
import javax.inject.Inject

private const val TAG = "FavoritesFragment"

@AndroidEntryPoint
class FavoritesFragment : BaseFragment<FragmentFavoriteBinding>(FragmentFavoriteBinding::inflate) {
    private val mNavController by lazy {
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }
    private val mViewModel: FavoritesViewModel by viewModels()

    @Inject
    lateinit var moshiHelper: MoshiHelper

    @Inject
    lateinit var appSharedPreference: AppSharedPreference
    override fun afterOnCreateView() {
        super.afterOnCreateView()
        setOnClickListeners()
    }

    private fun observeOnViewModel() {
        val currentLocationDetails = moshiHelper.getObjFromJsonString(LocationDetails::class.java,
            appSharedPreference.getStringValue(
                CURRENT_LOCATION, ""))
        currentLocationDetails?.let {
            mViewModel.getFavoritesWeather(it)
        }
        mViewModel.favoritesWeathers.removeObservers(viewLifecycleOwner)
        mViewModel.favoritesWeathers.observe(viewLifecycleOwner) {
            when (it) {
                null -> {
                    Log.e(TAG, "observeOnViewModel: nulls")
                }
                else -> {
                    binding.rvFavorites.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                        adapter = FavoritesAdapter(it) { w ->
                            mNavController.safeNavigation(R.id.mainFragment,
                                R.id.action_mainFragment_to_deleteFragmentDialog,
                                Bundle().apply {
                                    putParcelable(DELETE, LatLng(w.lat, w.lon))
                                })

                        }
                    }
                }
            }
        }
        mViewModel.exceptions.removeObservers(viewLifecycleOwner)
        mViewModel.exceptions.observe(viewLifecycleOwner) {
            when (it) {
                null -> Log.e(TAG, "observeOnViewModel: exception")
                is IllegalArgumentException -> {
                }
            }
        }
    }

    private fun setOnClickListeners() {
        binding.fabAddFavorite.setOnClickListener {
            mNavController.safeNavigation(R.id.mainFragment,
                R.id.action_mainFragment_to_placePickerFragment)
        }
    }

    override fun afterOnViewCreated() {
        super.afterOnViewCreated()
        observeOnViewModel()

        mNavController.currentBackStackEntry?.savedStateHandle?.getLiveData<LocationDetails>(
            LOCATION_KEY)?.removeObservers(viewLifecycleOwner)
        mNavController.currentBackStackEntry?.savedStateHandle?.getLiveData<LocationDetails>(
            LOCATION_KEY)?.observe(viewLifecycleOwner) {
            mViewModel.insertIntoFavorites(it, appSharedPreference.getStringValue(LOCALE, "en"))
        }



        //TODO what is this ?
//        mNavController.currentBackStackEntry?.savedStateHandle?.getLiveData<LatLng>(
//            DELETE)?.removeObservers(viewLifecycleOwner)
//        mNavController.currentBackStackEntry?.savedStateHandle?.getLiveData<LatLng>(
//            DELETE)?.observe(viewLifecycleOwner) {
//            mViewModel.deleteWeather(it)
//        }
    }
}