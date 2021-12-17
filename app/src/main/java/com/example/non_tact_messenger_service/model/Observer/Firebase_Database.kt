package com.example.non_tact_messenger_service.model.Observer

import android.content.Context
import android.util.Log
import com.example.non_tact_messenger_service.adapter.chat.ImageMessageItem
import com.example.non_tact_messenger_service.adapter.chat.TextMessageItem
import com.example.non_tact_messenger_service.model.*
import com.example.non_tact_messenger_service.model.Message.ImageMessage
import com.example.non_tact_messenger_service.model.Message.Message
import com.example.non_tact_messenger_service.model.Message.MessageType
import com.example.non_tact_messenger_service.model.Message.TextMessage
import com.example.non_tact_messenger_service.model.Users.Doctor
import com.example.non_tact_messenger_service.model.Users.Patient
import com.example.non_tact_messenger_service.model.Users.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.kotlinandroidextensions.Item

object Firebase_Database {
    var is_enabled = false
    val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    val currentUserDocRef: DocumentReference // 파이어베이스 어센틱케이션을 통해서 해당 파이어스토어의 document를 찾아간다.

        get() = firestoreInstance.document(
            "Users/${
                FirebaseAuth.getInstance().currentUser?.uid
                    ?: throw NullPointerException("UID is null.")
            }"
        )

    private val chatChannelsCollectionRef = firestoreInstance.collection("chat_room")

    fun initPatientUser(onComplete: () -> Unit) { //add Users
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                val newUser = User(
                    FirebaseAuth.getInstance().currentUser?.displayName ?: "",
                    "", false
                )
                val newPatient = Patient(
                    newUser, mutableListOf(), "", ""
                )
                currentUserDocRef.set(newPatient).addOnSuccessListener {
                    onComplete()
                }
            } else
                onComplete()
        }
    }

    fun initDoctorUser(onComplete: () -> Unit) { // add Doctor
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                val newUser = User(
                    FirebaseAuth.getInstance().currentUser?.displayName ?: "",
                    "", true
                )
                val newDoctor = Doctor(newUser, mutableListOf(), "")
                currentUserDocRef.set(newDoctor).addOnSuccessListener {
                    onComplete()
                }
            } else
                onComplete()
        }
    }
    fun updateCurrentUser(name: String = "", bio: String = "") { // update Users information
        val userFieldMap = mutableMapOf<String, Any>()
        val quserFieldMap = mutableMapOf<String, Any>()
        if (name.isNotBlank()) userFieldMap["name"] = name
        if (bio.isNotBlank()) userFieldMap["bio"] = bio
        userFieldMap["userType"] = true
        quserFieldMap["base_user"] = userFieldMap
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().currentUser!!.uid).update(quserFieldMap)
    }

    fun setPatientUser(health_title: String, health_detail: String) { // set Patient User
        val userFieldMap = mutableMapOf<String, Any>()
        if (health_title.isNotBlank() && health_detail.isNotBlank())
            userFieldMap["health_title"] = health_title
            userFieldMap["health_detail"] = health_detail
        currentUserDocRef.update(userFieldMap)
    }

    fun getDoctorUser(onComplete: (Doctor) -> Unit) { //get Doctor information for Doctor data class
        currentUserDocRef.get()
            .addOnSuccessListener {
                onComplete(it.toObject(Doctor::class.java)!!)
            }
    }

    fun getPatientUser(onComplete: (Patient) -> Unit) {
        currentUserDocRef.get()
            .addOnSuccessListener {
                onComplete(it.toObject(Patient::class.java)!!)
            }
    }


    fun getOrCreateChatChannel( //create new Chat_room or get Chat_room
        otherUserId: String,
        onComplete: (channelId: String) -> Unit
    ) {
        currentUserDocRef.collection("chating_room")
            .document(otherUserId).get().addOnSuccessListener {
                if (it.exists()) {
                    onComplete(it["channelId"] as String)
                    return@addOnSuccessListener
                }

                val currentUserId =
                    FirebaseAuth.getInstance().currentUser!!.uid // 현재 어플 사용자의 auth및 uid를 얻음
                val newChannel =
                    chatChannelsCollectionRef.document() //firestore 에 새로운 document(채팅방)추가
                newChannel.set(ChatChannel(mutableListOf(currentUserId, otherUserId)))

                currentUserDocRef // 현재 유저에게 채팅방 추가
                    .collection("chating_room")
                    .document(otherUserId)
                    .set(mapOf("channelId" to newChannel.id))

                firestoreInstance.collection("Users").document(otherUserId) // 다른방 유저에게 채팅방 추가
                    .collection("chating_room")
                    .document(currentUserId)
                    .set(mapOf("channelId" to newChannel.id))

                onComplete(newChannel.id)
            }
    }

    fun addChatMessagesListener( // 채팅방의 메세지 변화를 감지할 Observer
        channelId: String, context: Context?,
        onListen: (List<Item>) -> Unit
    ): ListenerRegistration {
        return chatChannelsCollectionRef.document(channelId).collection("messages")
            .orderBy("time") // 시간순 정렬
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) { //에러 예외처리
                    Log.e("FIRESTORE", "ChatMessagesListener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }
                val items = mutableListOf<Item>()
                querySnapshot!!.documents.forEach {
                    if (it["type"] == MessageType.TEXT) // 메세지 타입이 텍스트
                        items.add(
                            TextMessageItem( // Observer패턴에 의해 감지된 텍스트 메세지를 리스트에 추가
                                it.toObject(TextMessage::class.java)!!,
                                context!!
                            )
                        )
                    else
                        items.add( // Observer패턴에 의해 감지된 이미지 메세지를 리스트에 추가
                            ImageMessageItem(it.toObject(ImageMessage::class.java)!!, context!!)
                        )
                    return@forEach
                }
                onListen(items)
            }
    }

    fun sendMessage(message: Message, channelId: String) { // SendMessage to firestore
        chatChannelsCollectionRef.document(channelId)
            .collection("messages")
            .add(message)
    }

    fun getFCMRegistrationTokens(onComplete: (tokens: MutableList<String>) -> Unit) { // get FCM tokens for notifications
        currentUserDocRef.get().addOnSuccessListener {
            val user = it["registrationTokens"]
            onComplete(user as MutableList<String>)
        }
    }

    fun setFCMRegistrationTokens(registrationTokens: MutableList<String>) { // set FCM tokesn for notifications
        currentUserDocRef.update(mapOf("registrationTokens" to registrationTokens))
    }
}