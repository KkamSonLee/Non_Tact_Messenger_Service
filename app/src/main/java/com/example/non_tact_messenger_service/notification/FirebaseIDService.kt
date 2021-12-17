package com.example.non_tact_messenger_service.notification

import com.example.non_tact_messenger_service.model.Observer.Firebase_Database
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService

class FirebaseIDService : FirebaseMessagingService() { // notification service for FCM service

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        val newRegistrationToken = FirebaseMessaging.getInstance().token
        if (FirebaseAuth.getInstance().currentUser != null)
            addTokenToFirestore(newRegistrationToken.toString()) //임의로 토큰을 변환
    }

    companion object {
        fun addTokenToFirestore(newRegistrationToken: String?) {
            if (newRegistrationToken == null) throw NullPointerException("FCM token is null.")

            Firebase_Database.getFCMRegistrationTokens { tokens -> //token중복 검사
                if (tokens.contains(newRegistrationToken))
                    return@getFCMRegistrationTokens
                tokens.add(newRegistrationToken)
                Firebase_Database.setFCMRegistrationTokens(tokens)
            }
        }
    }
}