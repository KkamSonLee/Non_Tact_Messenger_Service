package com.example.non_tact_messenger_service.adapter.healthinfo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.non_tact_messenger_service.databinding.ItemHealthInfoBinding
import com.example.non_tact_messenger_service.model.Item_HealthInfo

class RecyclerviewPatientHealthAdapter( // adpater for Healthinfo RecyclerView
    val values: ArrayList<Item_HealthInfo>
) : RecyclerView.Adapter<RecyclerviewPatientHealthAdapter.ViewHolder>() {   //health information recyclerview 어뎁터
    var itemOnClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun OnItemClick(holder: RecyclerView.ViewHolder, view: View, data: Item_HealthInfo, position: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHealthInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("RestrictedApi")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) { // bindViewHolder with xml files
        val item = values[position]
        holder.health_title.text = item.health_title
        holder.health_detail.text = item.health_detail
    }

    override fun getItemCount(): Int = values.size // get ItemCount

    inner class ViewHolder(binding: ItemHealthInfoBinding): //inner class for recyclerview adapter
        RecyclerView.ViewHolder(binding.root) {
        val health_title: TextView = binding.healthsimple //binding TextView in item_health_info.xml
        val health_detail: TextView = binding.healthlong // binding TextView in item_health_info.xml

        init {
            binding.suggestion.setOnClickListener { //click event for suggestion btton
                itemOnClickListener?.OnItemClick(this, it, values[adapterPosition], adapterPosition)
            }
        }
    }

}