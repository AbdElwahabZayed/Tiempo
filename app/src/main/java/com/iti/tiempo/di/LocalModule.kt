package com.iti.tiempo.di

import android.content.Context
import androidx.room.Room
import com.iti.tiempo.local.WeatherDao
import com.iti.tiempo.local.WeatherDataBase
import com.iti.tiempo.utils.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule{

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): WeatherDataBase {
        return Room.databaseBuilder(context, WeatherDataBase::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Singleton
    @Provides
    fun parentDao(database: WeatherDataBase): WeatherDao {
        return database.weatherDao()
    }


}
