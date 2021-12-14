package com.example.non_tact_messenger_service.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.non_tact_messenger_service.MainActivity
import com.example.non_tact_messenger_service.R
import com.example.non_tact_messenger_service.databinding.FragmentSearchBinding
import com.example.non_tact_messenger_service.databinding.FragmentSelectBinding

class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        binding.search.setOnClickListener {
            Log.d("health title",binding.healthTitle.text.toString())
            (activity as (MainActivity)).fragmentChange(6)
        }
        return binding.root
    }
}
