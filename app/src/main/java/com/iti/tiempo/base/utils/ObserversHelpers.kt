package com.iti.tiempo.base.utils

import androidx.annotation.NonNull
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController

fun <T> NavController.observe(key:String,viewLifecycleOwner: LifecycleOwner,@NonNull observer:Observer<in T>){
    val savedStateHandler=currentBackStackEntry?.savedStateHandle
    savedStateHandler?.getLiveData<T>(key)?.removeObservers(viewLifecycleOwner)
    savedStateHandler?.getLiveData<T>(key)?.observe(viewLifecycleOwner,observer)
}