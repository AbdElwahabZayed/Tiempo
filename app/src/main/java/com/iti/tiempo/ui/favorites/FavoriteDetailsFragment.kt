package com.iti.tiempo.ui.favorites

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.iti.tiempo.R
import com.iti.tiempo.base.ui.BaseFragment
import com.iti.tiempo.databinding.FragmentFavoriteDetailsBinding
import com.iti.tiempo.local.AppSharedPreference
import com.iti.tiempo.model.DailyItem
import com.iti.tiempo.model.HourlyItem
import com.iti.tiempo.model.LocationDetails
import com.iti.tiempo.ui.home.view.HomeAdapter
import com.iti.tiempo.ui.home.viewmodel.HomeViewModel
import com.iti.tiempo.utils.CURRENT_LOCATION
import com.iti.tiempo.utils.ENGLISH
import com.iti.tiempo.utils.LOCALE
import com.iti.tiempo.utils.LOCATION
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TAG = "FavoriteDetailsFragment"

@AndroidEntryPoint
class FavoriteDetailsFragment :
    BaseFragment<FragmentFavoriteDetailsBinding>(FragmentFavoriteDetailsBinding::inflate) {
    private val mViewModel: HomeViewModel by viewModels()
    private var adapterData: MutableList<Any> = mutableListOf()
    private var mAdapter: HomeAdapter? = null
    private var favoriteLocation: LocationDetails? = null

    @Inject
    lateinit var appSharedPreference: AppSharedPreference

    override fun afterOnCreateView() {
        super.afterOnCreateView()
        mAdapter = HomeAdapter(adapterData, appSharedPreference, null)
        binding.rvHome.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mAdapter
        }
        favoriteLocation = arguments?.getParcelable(LOCATION)
        favoriteLocation?.let {
            mViewModel.getCurrentWeather(it, appSharedPreference.getStringValue(LOCALE,
                ENGLISH))
        }
        mViewModel.currentWeather.removeObservers(viewLifecycleOwner)
        mViewModel.currentWeather.observe(viewLifecycleOwner) { weather ->
            binding.swipeLayout.isRefreshing = false
            when (weather) {
                null -> {
                    Log.e(TAG, "afterOnViewCreated: ", NullPointerException())
                }
                else -> {
                    adapterData.clear()
                    val locationDetails = LocationDetails(LatLng(weather.lat, weather.lon),
                        weather.address ?: "none",
                        weather.lastDate ?: "none")
                    val weatherStatus = weather.current?.weather!![0]
                    val hours: List<HourlyItem>? = weather.hourly
                    val daily: List<DailyItem>? = weather.daily
                    val current = weather.current
                    adapterData.add(locationDetails)
                    adapterData.add(weatherStatus)
                    adapterData.add(hours!!)
                    adapterData.add(daily!!)
                    adapterData.add(current!!)

                    binding.rvHome.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                        adapter = mAdapter
                    }
                }
            }
        }
        binding.swipeLayout.setOnRefreshListener {

            when (favoriteLocation) {
                null -> {
                    binding.swipeLayout.isRefreshing = false
                    Toast.makeText(context,
                        context?.resources?.getString(R.string.some_thing_went_wrong),
                        Toast.LENGTH_SHORT).show()
                }
                else -> {
                    mViewModel.getCurrentWeather(favoriteLocation!!,
                        appSharedPreference.getStringValue(LOCALE, "en"))
                }
            }

        }

    }
}