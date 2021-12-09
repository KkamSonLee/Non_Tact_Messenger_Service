package com.example.non_tact_messenger_service.notification

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessageService : FirebaseMessagingService() {

        override fun onMessageReceived(remoteMessage: RemoteMessage) {
            if (remoteMessage.notification != null) {
                //TODO: Show notification if we're not online
                    val title = remoteMessage.notification!!.title
                val body = remoteMessage.notification!!.body
                Log.d("FCM", title.toString())
                Log.d("FCM", body.toString())
            }
        }
    }

