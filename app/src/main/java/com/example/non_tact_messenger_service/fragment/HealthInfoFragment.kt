package com.example.non_tact_messenger_service.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.non_tact_messenger_service.R
import com.example.non_tact_messenger_service.databinding.FragmentHealthInfoBinding

class HealthInfoFragment : Fragment() {
    lateinit var binding: FragmentHealthInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHealthInfoBinding.inflate(layoutInflater, container, false)
        binding.sendHealth.setOnClickListener {
            Log.d("health detail",binding.healthDetail.text.toString())

        }
        return binding.root
    }
}