package com.iti.tiempo.ui.favorites


import com.iti.tiempo.base.ui.BaseFragment
import com.iti.tiempo.databinding.FragmentFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment :BaseFragment<FragmentFavoriteBinding>(FragmentFavoriteBinding::inflate){
    override fun afterOnCreateView() {
    }
}