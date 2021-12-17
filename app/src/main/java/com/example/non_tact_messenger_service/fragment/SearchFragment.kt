package com.example.non_tact_messenger_service.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.non_tact_messenger_service.ViewController.MainActivity
import com.example.non_tact_messenger_service.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        binding.search.setOnClickListener {
            (activity as MainActivity).healthTitle = binding.healthTitle.text.toString()    //input health_title
            (activity as (MainActivity)).fragmentChange(6)  //change to HealthInfoFragment
        }
        return binding.root
    }
}
