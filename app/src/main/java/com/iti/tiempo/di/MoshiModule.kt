package com.iti.tiempo.di

import com.iti.tiempo.utils.MoshiHelper
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.FragmentScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MoshiModule  {

    @Provides
    @Singleton
    fun getMoshHelper(moshi: Moshi):MoshiHelper{
        return MoshiHelper(moshi)
    }

    @Provides
    @Singleton
    fun getMoshiObject():Moshi{
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build();
    }
}