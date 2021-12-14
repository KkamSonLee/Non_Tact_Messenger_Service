package com.example.non_tact_messenger_service

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.non_tact_messenger_service.databinding.FragmentSelectBinding
import com.example.non_tact_messenger_service.databinding.ItemHealthInfoBinding
import com.example.non_tact_messenger_service.model.HealthInfo
import java.util.*

class RecyclerviewPatientHealthAdapter(
    val values: ArrayList<HealthInfo>
) : RecyclerView.Adapter<RecyclerviewPatientHealthAdapter.ViewHolder>() {   //건강뉴스 recyclerview 어뎁터
    var itemOnClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun OnItemClick(holder: RecyclerView.ViewHolder, view: View, data: HealthInfo, position: Int)
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.simpleInfo.text = item.simple_Info
        holder.detailInfo.text = item.detail_Info
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: ItemHealthInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val simpleInfo: TextView = binding.simpleInfo
        val detailInfo: TextView = binding.detailInfo

        init {
            binding.suggestion.setOnClickListener {
                itemOnClickListener?.OnItemClick(this, it, values[adapterPosition], adapterPosition)
            }
        }
    }

}