package com.example.non_tact_messenger_service.ViewController

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.non_tact_messenger_service.databinding.ActivityChoiceBinding

class ChoiceActivity : AppCompatActivity() {
    lateinit var binding: ActivityChoiceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = Intent(this, MainActivity::class.java)
        binding.doctorBtn.setOnClickListener {   //when doctor
            intent.putExtra("user", true)
            startActivity(intent)    //to Change MainActivity with Extra user type
            finish()                //memory free
        }
        binding.patientBtn.setOnClickListener {   //when patient
            intent.putExtra("user", false)
            startActivity(intent)   //to Change MainActivity with Extra user type
            finish()                //memory free
        }
    }
}