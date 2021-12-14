package com.example.non_tact_messenger_service

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import com.example.non_tact_messenger_service.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import splitties.toast.toast
import android.R
import androidx.fragment.app.Fragment
import com.example.non_tact_messenger_service.chat.ChatFragment
import com.example.non_tact_messenger_service.fragment.*


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var intent_data:Int = 0
    private lateinit var auth: FirebaseAuth
    var fragment_Container =
        listOf<Fragment>(ProfileFragment(), SignupFragment(), SelectFragment(),ChatFragment(), DoctorCertifiedFragment(), HealthInfoFragment(), SearchFragment())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        auth = Firebase.auth
        if(intent.hasExtra("user")){
            fragmentChange(2)
            intent_data= intent.getIntExtra("user", 0)
        }else{
            fragmentChange(1)
        }

        setContentView(binding.root)
    }
    // 환자 - 구분 - > 회원가입 - > 건강정보 기입 -> 건강정보 리스트 and 채팅방(의사 프로필보기)
    // 환자(로그인 이후) - 건강정보 기입 and 건강정보 리스트 and 채팅방
    // 의료인 - 구분 -> 회원가입 -> 의료인 인증 -> 내 프로필 기입 -> 환자 건강정보 리스트 and 채팅방
    // 의료인(로그인 이후) - 환자 건강정보 리스트 and 채팅방
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload();
        }
    }
    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    toast("Authentication failed.")
                    updateUI(null)
                }
            }
        // [END create_user_with_email]
    }

    private fun sendEmailVerification() {
        // [START send_email_verification]
        val user = auth.currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                // Email Verification sent
            }
        // [END send_email_verification]
    }

    private fun updateUI(user: FirebaseUser?) {
    }

    private fun reload() {
    }

    fun fragmentChange(index: Int) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment_Container[index - 1]).commit()
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}