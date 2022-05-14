package com.iti.tiempo.di

import com.iti.tiempo.local.WeatherDao
import com.iti.tiempo.network.WeatherService
import com.iti.tiempo.repo.WeatherRepo
import com.iti.tiempo.repo.WeatherRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepoModule {
    @Provides
    @ViewModelScoped
    fun providesWeatherRepo(weatherService: WeatherService, weatherDao: WeatherDao): WeatherRepo {
        return WeatherRepoImpl(weatherService, weatherDao)
    }
}