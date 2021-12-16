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
import com.example.non_tact_messenger_service.util.Firebase_Database


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var intent_data:Int = 0
    var healthTitle:String = ""
    private lateinit var auth: FirebaseAuth
    var fragment_Container =
        listOf<Fragment>(ProfileFragment(), SignupFragment(), SelectFragment(),ChatFragment(), DoctorCertifiedFragment(), HealthInfoFragment(), SearchFragment())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        auth = Firebase.auth
        Firebase_Database.getHealthInfo()
        if(intent.hasExtra("user")){
            fragmentChange(2)
            intent_data= intent.getIntExtra("user", 0)
        }else{
            Firebase_Database.getCurrentUser {

            }
            fragmentChange(5)
        }
        setContentView(binding.root)
    }
    // 환자 - 구분(Choice Ac) - > 회원가입(Sign Fra) - > 증상입력(SearchFra) -> 건강정보 기입(HealthInfo Fra) -> 연결 대기(Buffering Fra) -> 채팅방(Chat Fra)
    // 환자(로그인 이후) - 증상입력(Search Fra) -> 건강정보 기입 -> 연결 대기 -> 채팅방
    // 의료인 - 구분 -> 회원가입 -> 의료인 인증(DoctorCerti Fra) -> 내 프로필 기입(Profile Fra) -> 환자 건강정보 리스트(Select Fra) -> 연결 대기(Buffering Fra) -> 채팅방(Chat Fra)
    // 의료인(로그인 이후) - 환자 건강정보 리스트 -> 연결 대기 -> 채팅방

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
