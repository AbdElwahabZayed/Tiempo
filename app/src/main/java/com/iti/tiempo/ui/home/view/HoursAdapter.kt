package com.iti.tiempo.ui.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import coil.request.ImageRequest
import com.iti.tiempo.base.utils.setTextWithCurrentTempValueWithType
import com.iti.tiempo.base.utils.setTimeForHourFromTimeStamp
import com.iti.tiempo.databinding.ItemHourWeatherBinding
import com.iti.tiempo.local.AppSharedPreference
import com.iti.tiempo.model.HourlyItem
import com.iti.tiempo.utils.WEATHER_API_IMAGE_ENDPOINT

class HoursAdapter(
    private val hours: List<HourlyItem>,
    val appSharedPreference: AppSharedPreference,
) :
    RecyclerView.Adapter<HoursAdapter.HoursViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoursViewHolder {
        return HoursViewHolder(ItemHourWeatherBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false), appSharedPreference)
    }

    override fun onBindViewHolder(holder: HoursViewHolder, position: Int) {
        holder.onBind(hours[position])
    }

    override fun getItemCount() = hours.size

    class HoursViewHolder(
        val item: ItemHourWeatherBinding,
        val appSharedPreference: AppSharedPreference,
    ) : RecyclerView.ViewHolder(item.root) {
        fun onBind(hour: HourlyItem) {
            item.textViewHour.setTimeForHourFromTimeStamp(hour.dt)
            item.textViewTemp.setTextWithCurrentTempValueWithType(appSharedPreference, hour.temp)
            val link = WEATHER_API_IMAGE_ENDPOINT + hour.weather[0].icon + ".png"
            val uri = link.toUri().buildUpon().scheme("https").build()
            val request = ImageRequest.Builder(item.root.context)
                .data(link)
                .target(
                    onSuccess = { result ->
                        item.textViewTemp.compoundDrawablesRelative[1] = result
                    },
                )
                .build()
            item.root.context.imageLoader.enqueue(request)
        }
    }
}