package com.iti.tiempo

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.iti.tiempo.base.utils.LocaleUtil
import com.iti.tiempo.local.AppSharedPreference
import com.iti.tiempo.utils.ENGLISH
import com.iti.tiempo.utils.LOCALE
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MainApp : Application(), Configuration.Provider  {


    @Inject lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    override fun attachBaseContext(base: Context) {
        val sharedPreference = base.getSharedPreferences(
            base.getString(R.string.app_name) + "1",
            Context.MODE_PRIVATE
        )

        super.attachBaseContext(LocaleUtil.getLocalizedContext(base,
            AppSharedPreference(sharedPreference, sharedPreference.edit()).getStringValue(
                LOCALE, ENGLISH)))
    }
}