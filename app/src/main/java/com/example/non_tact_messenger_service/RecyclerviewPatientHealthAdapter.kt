package com.example.non_tact_messenger_service

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.non_tact_messenger_service.databinding.ItemHealthInfoBinding
import com.example.non_tact_messenger_service.model.Item_HealthInfo
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().currentUser!!.uid).get().addOnSuccessListener {
            var user = it.get("base_user")
            val objec = user as MutableMap<String, Any>
            name = objec["name"] as String
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