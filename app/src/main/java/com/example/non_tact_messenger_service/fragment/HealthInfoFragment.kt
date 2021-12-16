package com.example.non_tact_messenger_service.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.non_tact_messenger_service.MainActivity
import com.example.non_tact_messenger_service.R
import com.example.non_tact_messenger_service.databinding.FragmentHealthInfoBinding
import com.example.non_tact_messenger_service.util.Firebase_Database
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

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
            (activity as MainActivity).fragmentChange(4)
        }
        return binding.root
    }
}