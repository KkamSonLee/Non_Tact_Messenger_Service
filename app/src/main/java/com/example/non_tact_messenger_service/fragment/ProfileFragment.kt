package com.example.non_tact_messenger_service.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.non_tact_messenger_service.ViewController.MainActivity
import com.example.non_tact_messenger_service.databinding.FragmentProfileBinding
import com.example.non_tact_messenger_service.model.Firebase_Database


class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    var mainActivity: MainActivity? = null

    // 메인 액티비티 위에 올린다.
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = activity as MainActivity
    }

    // 메인 액티비티에서 내려온다.
    override fun onDetach() {
        super.onDetach()
        mainActivity = null
        binding.editTextName.setText("")
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        binding.btnSave.setOnClickListener {
            Firebase_Database.updateCurrentUser(
                binding.editTextName.text.toString(),
                binding.editTextDepartment.text.toString()
            )
            (activity as MainActivity).fragmentChange(3)
        }
        return binding.root
    }

    @SuppressLint("RestrictedApi")
    override fun onStart() {
        super.onStart()
        Log.d("onStart", "buffering")
        Firebase_Database.getDoctorUser { user ->
            if (this@ProfileFragment.isVisible) {
                binding.editTextName.setText(user.base_user.name)
                binding.editTextDepartment.setText(user.base_user.bio)

            }
        }
    }
}

