package com.example.non_tact_messenger_service.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.non_tact_messenger_service.Doctor_Activity
import com.example.non_tact_messenger_service.Patient_Activity
import com.example.non_tact_messenger_service.R
import com.example.non_tact_messenger_service.databinding.FragmentSelectBinding

class SelectFragment : Fragment() {  //환자 리스트 선택

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select, container, false)
    }
}
