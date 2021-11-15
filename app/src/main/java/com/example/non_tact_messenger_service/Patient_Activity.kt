package com.example.non_tact_messenger_service

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.non_tact_messenger_service.databinding.ActivityPatientBinding

class Patient_Activity : AppCompatActivity() {
    lateinit var binding: ActivityPatientBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}