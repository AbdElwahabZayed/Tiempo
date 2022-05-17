package com.iti.tiempo.base.ui

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.iti.tiempo.R
import com.iti.tiempo.base.utils.LocaleUtil
import com.iti.tiempo.local.AppSharedPreference
import com.iti.tiempo.utils.ENGLISH
import com.iti.tiempo.utils.LOCALE
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {
    private lateinit var oldPrefLocaleCode: String

    lateinit var appSharedPreference: AppSharedPreference

    /**
     * updates the toolbar text locale if it set from the android:label property of Manifest
     */
    private fun resetTitle() {
        try {
            val label = packageManager.getActivityInfo(componentName,
                PackageManager.GET_META_DATA).labelRes;
            if (label != 0) {
                setTitle(label);
            }
        } catch (e: PackageManager.NameNotFoundException) {
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val sharedPreference = newBase.getSharedPreferences(
            newBase.getString(R.string.app_name) + "1",
            Context.MODE_PRIVATE
        )
        appSharedPreference = AppSharedPreference(sharedPreference, sharedPreference.edit())
        oldPrefLocaleCode = appSharedPreference.getStringValue(LOCALE, ENGLISH)
        applyOverrideConfiguration(LocaleUtil.getLocalizedConfiguration(oldPrefLocaleCode))
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resetTitle()
    }

    override fun onResume() {
        val currentLocaleCode = appSharedPreference.getStringValue(LOCALE, ENGLISH)
        if (oldPrefLocaleCode != currentLocaleCode) {
            recreate() //locale is changed, restart the activty to update
            oldPrefLocaleCode = currentLocaleCode
        }
        super.onResume()
    }

}