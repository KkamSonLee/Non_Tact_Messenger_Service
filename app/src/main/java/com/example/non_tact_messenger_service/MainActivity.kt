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
    private lateinit var auth: FirebaseAuth
    var fragment_Container =
        listOf<Fragment>(ProfileFragment(), SignupFragment(), SelectFragment(),ChatFragment())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        binding.b1.setOnClickListener {
            fragmentChange(1)

        }
        binding.b2.setOnClickListener {
            fragmentChange(2)
        }
        binding.b3.setOnClickListener {
            fragmentChange(3)
        }
        binding.b4.setOnClickListener{
            fragmentChange(4)
        }

    }

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