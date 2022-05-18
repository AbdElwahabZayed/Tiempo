package com.iti.tiempo.ui.alarm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iti.tiempo.base.utils.setDateFromTimeStamp
import com.iti.tiempo.base.utils.setTimeForHourFromTimeStamp
import com.iti.tiempo.databinding.ItemAlarmBinding
import com.iti.tiempo.local.AppSharedPreference
import com.iti.tiempo.model.Alarm

private const val TAG = "AlarmsAdapter"
class AlarmsAdapter(val list:List<Alarm>,val appSharedPreference: AppSharedPreference,val onClickMore:(Alarm)->Unit) : RecyclerView.Adapter<AlarmsAdapter.AlarmViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        return AlarmViewHolder(ItemAlarmBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = list[position]
        holder.item.btnDelete.setOnClickListener {
            onClickMore(alarm)
        }
        holder.item.textViewStartDate.setDateFromTimeStamp(alarm.fromDate)
        holder.item.textViewEndDate.setDateFromTimeStamp(alarm.toDate)
        holder.item.textViewTime.setTimeForHourFromTimeStamp(alarm.time,appSharedPreference,1)
    }

    override fun getItemCount() = list.size

    class AlarmViewHolder(val item: ItemAlarmBinding) : RecyclerView.ViewHolder(item.root)
}