package com.iti.tiempo.ui.favorites

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.tiempo.databinding.ItemFavoriteBinding
import com.iti.tiempo.model.WeatherResponse

private const val TAG = "FavoritesAdapter"
class FavoritesAdapter(val list:List<WeatherResponse>,val onClickMore:(WeatherResponse)->Unit) : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        return FavoritesViewHolder(ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val weather = list[position]
        Log.e(TAG, "onBindViewHolder: ${weather.address}")
        holder.item.textViewLocationName.text = weather.address
        holder.item.btnDelete.setOnClickListener {
            onClickMore(weather)
        }
    }

    override fun getItemCount() = list.size

    class FavoritesViewHolder(val item: ItemFavoriteBinding) : RecyclerView.ViewHolder(item.root)
}