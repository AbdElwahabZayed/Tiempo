package com.iti.tiempo.ui.settings

import androidx.navigation.Navigation
import com.iti.tiempo.R
import com.iti.tiempo.base.ui.BaseFragment
import com.iti.tiempo.base.utils.getDateHomeStyleString
import com.iti.tiempo.base.utils.safeNavigation
import com.iti.tiempo.databinding.FragmentSettingsBinding
import com.iti.tiempo.local.AppSharedPreference
import com.iti.tiempo.model.LocationDetails
import com.iti.tiempo.ui.placepicker.PlacePickerFragment
import com.iti.tiempo.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {


    private val mNavController by lazy {
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }

    @Inject
    lateinit var appSharedPreference: AppSharedPreference

    @Inject
    lateinit var moshiHelper: MoshiHelper
    override fun afterOnCreateView() {
        super.afterOnCreateView()
        setupView()
        setListeners()
    }

    private fun setListeners() {
        binding.rbArabic.setOnClickListener{
            setAppLocale(ARABIC)
            appSharedPreference.setValue(LOCALE, ARABIC)
        }
        binding.rbEnglish.setOnClickListener {
            setAppLocale(ENGLISH)
            appSharedPreference.setValue(LOCALE, ENGLISH)
        }
        binding.rbGps.setOnClickListener {
            appSharedPreference.setValue(TYPE_OF_LOCATION,GPS)
        }
        binding.rbMap.setOnClickListener {
            appSharedPreference.setValue(TYPE_OF_LOCATION, MAP)
            mNavController.safeNavigation(R.id.mainFragment,R.id.action_mainFragment_to_placePickerFragment)
        }
        binding.rbMeter.setOnClickListener {
            appSharedPreference.setValue(WIND_SPEED_TYPE, METER_SEC)
        }
        binding.rbMile.setOnClickListener {
            appSharedPreference.setValue(WIND_SPEED_TYPE, MILE_HOUR)
        }
        binding.rbFahrenheit.setOnClickListener {
            appSharedPreference.setValue(CURRENT_TEMP_TYPE, Fahrenheit)
        }
        binding.rbCelsius.setOnClickListener {
            appSharedPreference.setValue(CURRENT_TEMP_TYPE, Celsius)
        }
        binding.rbKelvin.setOnClickListener {
            appSharedPreference.setValue(CURRENT_TEMP_TYPE, Kelvin)
        }
        binding.rbEnable.setOnClickListener {
            appSharedPreference.setValue(IS_NOTIFICATION_ENABLED, true)
        }
        binding.rbDisable.setOnClickListener {
            appSharedPreference.setValue(IS_NOTIFICATION_ENABLED, false)
        }
    }

    private fun setupView() {
        val lang = appSharedPreference.getStringValue(LOCALE, "en")
        val locationType = appSharedPreference.getStringValue(TYPE_OF_LOCATION, GPS)
        val windSpeedType = appSharedPreference.getStringValue(WIND_SPEED_TYPE, METER_SEC)
        val tempType = appSharedPreference.getStringValue(CURRENT_TEMP_TYPE, Fahrenheit)
        val isNotificationEnabled =
            appSharedPreference.getBooleanValue(IS_NOTIFICATION_ENABLED, true)
        when (lang) {
            ARABIC -> binding.rbArabic.isChecked = true
            ENGLISH -> binding.rbEnglish.isChecked = true
        }
        when (locationType) {
            GPS -> binding.rbGps.isChecked = true
            MAP -> binding.rbMap.isChecked = true
        }
        when (windSpeedType) {
            METER_SEC -> binding.rbMeter.isChecked = true
            MILE_HOUR -> binding.rbMile.isChecked = true
        }
        when (tempType) {
            Fahrenheit -> binding.rbFahrenheit.isChecked = true
            Celsius -> binding.rbCelsius.isChecked = true
            Kelvin -> binding.rbKelvin.isChecked = true
        }
        when (isNotificationEnabled) {
            true -> binding.rbEnable.isChecked = true
            else -> binding.rbDisable.isChecked = false
        }
    }

    override fun afterOnViewCreated() {
        super.afterOnViewCreated()
        mNavController.currentBackStackEntry?.savedStateHandle?.getLiveData<LocationDetails>(
            PlacePickerFragment.LOCATION_KEY)?.removeObservers(viewLifecycleOwner)
        mNavController.currentBackStackEntry?.savedStateHandle?.getLiveData<LocationDetails>(
            PlacePickerFragment.LOCATION_KEY)?.observe(viewLifecycleOwner) {
            appSharedPreference.setValue(
                CURRENT_LOCATION,
                moshiHelper.getJsonStringFromObject(LocationDetails::class.java,
                    it)
            )
        }
    }

}