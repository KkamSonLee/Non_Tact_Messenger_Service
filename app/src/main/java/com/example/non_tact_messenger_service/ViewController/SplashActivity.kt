package com.example.non_tact_messenger_service.ViewController

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.non_tact_messenger_service.R
import com.google.firebase.auth.FirebaseAuth
import splitties.activities.start

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            if (FirebaseAuth.getInstance().currentUser == null)    //my Auth not exist
                start<ChoiceActivity>()   //sign up
            else
                start<MainActivity>()     //change to MainActivity
            finish()
        }, 2000L)
    }
}
