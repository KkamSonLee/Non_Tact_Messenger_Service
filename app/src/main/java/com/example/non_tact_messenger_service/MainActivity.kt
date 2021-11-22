package com.example.non_tact_messenger_service

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.non_tact_messenger_service.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.doctorBtn.setOnClickListener {
            var intent = Intent(this, Doctor_Activity::class.java)
            startActivity(intent)
            finish()
        }
        binding.patientBtn.setOnClickListener {
            var intent = Intent(this, Patient_Activity::class.java)
            startActivity(intent)
            finish()
        }
        setContentView(binding.root)
    }
}