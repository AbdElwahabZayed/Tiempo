package com.iti.tiempo.di

import com.iti.tiempo.utils.MoshiHelper
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
object MoshiModule  {

    @Provides
    @FragmentScoped
    fun getMoshHelper(moshi: Moshi):MoshiHelper{
        return MoshiHelper(moshi)
    }

    @Provides
    @FragmentScoped
    fun getMoshiObject():Moshi{
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build();
    }
}