package com.iti.tiempo.di

import android.content.Context
import androidx.work.WorkManager
import com.iti.tiempo.local.AlarmDao
import com.iti.tiempo.local.WeatherDao
import com.iti.tiempo.network.WeatherService
import com.iti.tiempo.repo.AlarmRepo
import com.iti.tiempo.repo.AlarmRepoImpl
import com.iti.tiempo.repo.WeatherRepo
import com.iti.tiempo.repo.WeatherRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {
    @Provides
    @Singleton
    fun providesWeatherRepo(weatherService: WeatherService, weatherDao: WeatherDao): WeatherRepo {
        return WeatherRepoImpl(weatherService, weatherDao)
    }

    @Provides
    @Singleton
    fun providesAlarmRepo(alarmDao: AlarmDao, @ApplicationContext context: Context): AlarmRepo {
        return AlarmRepoImpl(alarmDao, WorkManager.getInstance(context))
    }
}