package com.example.non_tact_messenger_service.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.non_tact_messenger_service.util.Firebase_Database
import com.example.non_tact_messenger_service.MainActivity
import com.example.non_tact_messenger_service.R
import com.example.non_tact_messenger_service.WebAppInterface
import com.example.non_tact_messenger_service.databinding.FragmentDoctorCertifiedBinding
import com.example.non_tact_messenger_service.databinding.FragmentSignupBinding
import com.example.non_tact_messenger_service.notification.FirebaseIDService
import com.example.non_tact_messenger_service.notification.FirebaseMessageService

import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.rpc.context.AttributeContext
import splitties.fragments.addToBackStack
import splitties.fragments.start


class SignupFragment : Fragment() {
    private val RC_SIGN_IN = 1
    lateinit var binding: FragmentSignupBinding
    var receive_data:Boolean = false //
    private val signInProviders =
        listOf(
            AuthUI.IdpConfig.EmailBuilder()
                .setAllowNewAccounts(true)
                .setRequireName(true)
                .build()
        )
    var mainActivity: MainActivity? = null

    // 메인 액티비티 위에 올린다.
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = activity as MainActivity
        if(mainActivity?.intent_data==false){
            Log.d("user", "null")   //환자
        }else{   //의료인
            receive_data = mainActivity?.intent_data!!
        }
    }

    // 메인 액티비티에서 내려온다.
    override fun onDetach() {
        super.onDetach()
        mainActivity = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(layoutInflater, container, false)
        binding.joinBtn.setOnClickListener {
            val intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(signInProviders)
                .setIsSmartLockEnabled(false)
                .build()
            startActivityForResult(intent, RC_SIGN_IN)
        }
        return binding.root
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                val auth = FirebaseAuth.getInstance().currentUser
                if(receive_data){  //의사일때
                    Firebase_Database.currentUserDocRef.get().addOnSuccessListener {
                        if(it["license"].toString() == ""){
                            Firebase_Database.initDoctorUser {
                                val registrationToken = FirebaseMessaging.getInstance().token
                                FirebaseIDService.addTokenToFirestore(registrationToken.toString())
                                mainActivity?.fragmentChange(5)
                            }
                        }else{
                            mainActivity?.fragmentChange(3)
                        }
                    }
                }else{
                    Firebase_Database.initPatientUser {
                        val registrationToken = FirebaseMessaging.getInstance().token
                        FirebaseIDService.addTokenToFirestore(registrationToken.toString())
                        mainActivity?.fragmentChange(7)
                    }

                }
            }

        } else if (resultCode == Activity.RESULT_CANCELED) {
            val response = IdpResponse.fromResultIntent(data)
            if (response == null) return
            when (response.error?.errorCode) {
                ErrorCodes.NO_NETWORK ->
                    Snackbar.make(requireView(), "No network", Snackbar.LENGTH_SHORT).show()
                ErrorCodes.UNKNOWN_ERROR ->
                    Snackbar.make(requireView(), "Unknown error", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}