package com.example.non_tact_messenger_service.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.non_tact_messenger_service.MainActivity
import com.example.non_tact_messenger_service.databinding.FragmentHealthInfoBinding
import com.example.non_tact_messenger_service.util.Firebase_Database
import com.google.firebase.firestore.FirebaseFirestore

class HealthInfoFragment : Fragment() {
    lateinit var binding: FragmentHealthInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHealthInfoBinding.inflate(layoutInflater, container, false)

        binding.sendHealth.setOnClickListener {
            Firebase_Database.setPatientUser((activity as MainActivity).healthTitle,
                binding.healthDetail.text.toString())
            if((activity as MainActivity).otherUID.isNullOrBlank()){
                Firebase_Database.currentUserDocRef.collection("chating_room").get().addOnSuccessListener {
                    it.forEach {
                        (activity as MainActivity).otherUID = it.id
                    }
                }
                Toast.makeText(context, "등록이 완료되었습니다. 의사가 제안 할 때 까지 조금만 대기해주세요.", Toast.LENGTH_SHORT).show()
            }else{
            (activity as MainActivity).fragmentChange(4)
            }
        }
        return binding.root
    }
}