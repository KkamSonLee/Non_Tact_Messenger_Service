package com.example.non_tact_messenger_service

import android.annotation.SuppressLint
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
import com.example.non_tact_messenger_service.model.Item_HealthInfo
import com.example.non_tact_messenger_service.util.Firebase_Database
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class RecyclerviewPatientHealthAdapter(
    val values: ArrayList<Item_HealthInfo>
) : RecyclerView.Adapter<RecyclerviewPatientHealthAdapter.ViewHolder>() {   //건강뉴스 recyclerview 어뎁터
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
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        var name:String = ""
        FirebaseFirestore.getInstance().collection("Users").document(item.uid.toString()).get().addOnSuccessListener {
            name = (it["base_user"] as User)!!.name.toString()
        }
        holder.health_title.text = name+"-"+item.health_title
        holder.health_detail.text = item.health_detail
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: ItemHealthInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val health_title: TextView = binding.healthsimple
        val health_detail: TextView = binding.healthlong

        init {
            binding.suggestion.setOnClickListener {
                itemOnClickListener?.OnItemClick(this, it, values[adapterPosition], adapterPosition)
            }
        }
    }

}