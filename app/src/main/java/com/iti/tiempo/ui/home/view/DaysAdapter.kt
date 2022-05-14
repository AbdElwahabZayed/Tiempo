    package com.iti.tiempo.ui.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import coil.request.ImageRequest
import com.iti.tiempo.R
import com.iti.tiempo.base.utils.setTextToDayFromTimeStamp
import com.iti.tiempo.base.utils.setTextWithCurrentTempValueWithType
import com.iti.tiempo.base.utils.setTimeForHourFromTimeStamp
import com.iti.tiempo.databinding.ItemDayWeatherBinding
import com.iti.tiempo.local.AppSharedPreference
import com.iti.tiempo.model.DailyItem
import com.iti.tiempo.utils.WEATHER_API_IMAGE_ENDPOINT

class DaysAdapter(private val days: List<DailyItem>, val appSharedPreference: AppSharedPreference) :
    RecyclerView.Adapter<DaysAdapter.DaysViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysViewHolder {
        return DaysViewHolder(ItemDayWeatherBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false), appSharedPreference)
    }

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {
        holder.onBind(days[position],position)
    }

    override fun getItemCount() = days.size

    class DaysViewHolder(
        val item: ItemDayWeatherBinding,
        val appSharedPreference: AppSharedPreference,
    ) : RecyclerView.ViewHolder(item.root) {
        fun onBind(day: DailyItem, position: Int) {
            if (position==0)
                item.containerLayout.background = ContextCompat.getDrawable(item.root.context,R.drawable.button_background_gradient)
            item.textViewDay.setTextToDayFromTimeStamp(day.dt)
            item.textViewTemp.setTextWithCurrentTempValueWithType(appSharedPreference, day.temp)
            item.textViewStatus.text = day.weather[0].description
            val request = ImageRequest.Builder(item.root.context)
                .data(WEATHER_API_IMAGE_ENDPOINT + day.weather[0].icon)
                .target(
                    onSuccess = { result ->
                        item.textViewStatus.compoundDrawablesRelative[0] = result
                    },
                )
                .build()
            item.root.context.imageLoader.enqueue(request)
        }
    }
}