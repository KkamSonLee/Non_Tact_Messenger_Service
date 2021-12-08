package com.example.non_tact_messenger_service

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.example.non_tact_messenger_service.chat.ChatFragment
import com.example.non_tact_messenger_service.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView,ChatFragment(),null)
            .addToBackStack(null)
            .commit()
    }


}
