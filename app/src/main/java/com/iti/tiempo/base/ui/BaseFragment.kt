package com.iti.tiempo.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseFragment<T : ViewBinding>(private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> T)  : Fragment() {
    private var _binding: T? = null
    val binding get() = _binding!!
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(layoutInflater,container,false)
        afterOnCreateView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        afterOnViewCreated()
    }

    open fun afterOnViewCreated() {

    }

    open fun afterOnCreateView(){

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}