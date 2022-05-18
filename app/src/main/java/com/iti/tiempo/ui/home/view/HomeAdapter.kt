package com.iti.tiempo.ui.home.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.iti.tiempo.base.ui.BaseAdapterViewHolder
import com.iti.tiempo.base.utils.*
import com.iti.tiempo.databinding.*
import com.iti.tiempo.local.AppSharedPreference
import com.iti.tiempo.model.*
import com.iti.tiempo.network.exceptions.MyException
import com.iti.tiempo.utils.*
import java.lang.IllegalArgumentException

interface OnClickActionListener {
    fun onClickEnable()
    fun onClickAllow()
}

@Suppress("UNCHECKED_CAST")
class HomeAdapter(
    private var adapterData: MutableList<Any>,
    val appSharedPreference: AppSharedPreference,
    val onClickActionListener: OnClickActionListener?,
) :
    RecyclerView.Adapter<BaseAdapterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapterViewHolder {
        return when (viewType) {
            ITEM_CURRENT_CITY_DETAILS -> CurrentCityDetailsViewHolder(ItemCurrentCityDetailsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
            ITEM_WEATHER_STATUS -> WeatherStatusViewHolder(ItemWeatherStatusBinding.inflate(
                LayoutInflater.from(parent.context), parent, false), appSharedPreference)
            ITEM_DAYS -> DaysViewHolder(ItemDaysRvBinding.inflate(
                LayoutInflater.from(parent.context), parent, false), appSharedPreference)
            ITEM_HOURS -> HoursViewHolder(ItemHourlyWeatherBinding.inflate(
                LayoutInflater.from(parent.context), parent, false), appSharedPreference)
            ITEM_WEATHER_DETAILS -> WeatherDetailsViewHolder(ItemWeatherDetailsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false), appSharedPreference)
            NO_PERMISSION -> NoPermissionViewHolder(ItemAllowAccessLocationBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
            GPSIsDisabled -> GpsIsDisabledViewHolder(ItemGpsIsDisabledBinding.inflate(LayoutInflater.from(
                parent.context), parent, false))
            else -> {
                Log.e("TAG", "onCreateViewHolder: $viewType")
                throw IllegalArgumentException()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {//TODO add no network _ no gps_opened

        return when (val data = adapterData[position]) {
            is LocationDetails -> ITEM_CURRENT_CITY_DETAILS
            is WeatherItem -> ITEM_WEATHER_STATUS
            is Current -> ITEM_WEATHER_DETAILS
            is MyException.NoGPSPermission -> NO_PERMISSION
            is MyException.GPSIsDisabled -> GPSIsDisabled
            else -> when ((data as List<*>)[0]) {
                is HourlyItem -> ITEM_HOURS
                is DailyItem -> ITEM_DAYS
                else -> UNSUPPORTED - 5
            }

        }
    }

    override fun onBindViewHolder(holder: BaseAdapterViewHolder, position: Int) {
        val data = adapterData[position]
        when (holder) {
            is CurrentCityDetailsViewHolder -> holder.onBind(data as LocationDetails)
            is WeatherStatusViewHolder -> holder.onBind(data as WeatherItem)
            is HoursViewHolder -> holder.onBind(data as List<HourlyItem>)
            is WeatherDetailsViewHolder -> holder.onBind(data as Current)
            is DaysViewHolder -> holder.onBind(data as List<DailyItem>)
            is NoPermissionViewHolder ->
                holder.item.btnAllow.setOnClickListener {
                    onClickActionListener?.onClickAllow()
                }

            is GpsIsDisabledViewHolder -> holder.item.btnEnable.setOnClickListener{
                onClickActionListener?.onClickEnable()
            }
        }
    }

    override fun getItemCount() = adapterData.size
    class CurrentCityDetailsViewHolder(private val item: ItemCurrentCityDetailsBinding) :
        BaseAdapterViewHolder(itemView = item.root) {
        fun onBind(locationDetails: LocationDetails) {
            item.textViewCityName.text = locationDetails.address
            item.textViewDate.text = locationDetails.lastDate
        }
    }

    class WeatherStatusViewHolder(
        private val item: ItemWeatherStatusBinding,
        val appSharedPreference: AppSharedPreference,
    ) :
        BaseAdapterViewHolder(itemView = item.root) {
        fun onBind(weatherItem: WeatherItem) {
            val request = ImageRequest.Builder(item.root.context)
                .data(WEATHER_API_IMAGE_ENDPOINT + weatherItem.icon + ".png")
                .crossfade(true)
                .target(item.imgViewCityWeather)
                .build()
//            load(WEATHER_API_IMAGE_ENDPOINT + weatherItem.icon + ".png")
            item.imgViewCityWeather.context.imageLoader.enqueue(request)
            item.textViewTempType.setTextWithCurrentTempType(appSharedPreference)
            item.textViewTempValue.setTextWithCurrentTempValue(appSharedPreference,
                weatherItem.temp ?: 0.0)
        }
    }

    class DaysViewHolder(
        val item: ItemDaysRvBinding,
        val appSharedPreference: AppSharedPreference,
    ) :
        BaseAdapterViewHolder(itemView = item.root) {
        fun onBind(list: List<DailyItem>) {
            item.rvDaysWeather.apply {
                setHasFixedSize(true)
                layoutManager =
                    LinearLayoutManager(item.root.context, RecyclerView.VERTICAL, false)
                adapter = DaysAdapter(list, appSharedPreference)
            }
        }
    }

    class HoursViewHolder(
        val item: ItemHourlyWeatherBinding,
        val appSharedPreference: AppSharedPreference,
    ) :
        BaseAdapterViewHolder(itemView = item.root) {
        fun onBind(list: List<HourlyItem>) {
            item.rvHourlyWeather.apply {
                setHasFixedSize(true)
                layoutManager =
                    LinearLayoutManager(item.root.context, RecyclerView.HORIZONTAL, false)
                adapter = HoursAdapter(list, appSharedPreference)
            }
        }
    }

    class WeatherDetailsViewHolder(
        val item: ItemWeatherDetailsBinding,
        val appSharedPreference: AppSharedPreference,
    ) :
        BaseAdapterViewHolder(itemView = item.root) {
        fun onBind(current: Current) {
            item.textViewCloud.setValueWithPercentage(current.clouds)
            item.textViewHumidity.setValueWithPercentage(current.humidity)
            item.textViewUltra.text = "${current.uvi}"
            item.textViewVisibility.setVisibilityWithUnit(current.visibility)
            item.textViewWind.setWindWithUnit(appSharedPreference, current.windSpeed)
            item.textViewPressure.setPressureWithUnit(current.pressure)
        }
    }

    class NoPermissionViewHolder(val item: ItemAllowAccessLocationBinding) :
        BaseAdapterViewHolder(itemView = item.root)

    class GpsIsDisabledViewHolder(val item: ItemGpsIsDisabledBinding) :
        BaseAdapterViewHolder(itemView = item.root)
}