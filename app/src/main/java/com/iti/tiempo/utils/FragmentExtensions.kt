package com.iti.tiempo.utils

import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppLocalesMetadataHolderService
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import java.util.*


fun Fragment.setAppLocale(localeCode: String) {
    val locale = Locale(localeCode)
    val config = resources.configuration
    config.setLocale(locale)
    config.setLayoutDirection(locale)
    requireContext().createConfigurationContext(config);
    requireActivity().recreate()
}