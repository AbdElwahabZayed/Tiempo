package com.iti.tiempo.ui.settings

import com.iti.tiempo.base.ui.BaseFragment
import com.iti.tiempo.databinding.FragmentSettingsBinding
import com.iti.tiempo.local.AppSharedPreference
import com.iti.tiempo.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {
    @Inject
    lateinit var appSharedPreference: AppSharedPreference

    @Inject
    lateinit var moshi: MoshiHelper
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
        }
        binding.rbMeter.setOnClickListener {

        }
        binding.rbMile.setOnClickListener {

        }
        binding.rbFahrenheit.setOnClickListener {

        }
        binding.rbCelsius.setOnClickListener {

        }
        binding.rbKelvin.setOnClickListener {

        }
        binding.rbEnable.setOnClickListener {

        }
        binding.rbDisable.setOnClickListener {

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
            MILE_SEC -> binding.rbMile.isChecked = true
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

}