package com.example.non_tact_messenger_service.util

import android.content.Context
import android.util.Log
import com.example.non_tact_messenger_service.MainActivity
import com.example.non_tact_messenger_service.chat.*
import com.example.non_tact_messenger_service.chat.Message
import com.example.non_tact_messenger_service.chat.model.ImageMessage
import com.example.non_tact_messenger_service.chat.model.TextMessage
import com.example.non_tact_messenger_service.chat.model.User
import com.example.non_tact_messenger_service.chat.recycler.ImageMessageItem
import com.example.non_tact_messenger_service.chat.recycler.TextMessageItem
import com.example.non_tact_messenger_service.model.Doctor
import com.example.non_tact_messenger_service.model.HealthInfo
import com.example.non_tact_messenger_service.model.Item_HealthInfo
import com.example.non_tact_messenger_service.model.Patient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.xwray.groupie.kotlinandroidextensions.Item

object Firebase_Database {

    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val currentUserDocRef: DocumentReference // 파이어베이스 어센틱케이션을 통해서 해당 파이어스토어의 document를 찾아간다.
        get() = firestoreInstance.document(
            "Users/${
                FirebaseAuth.getInstance().currentUser?.uid
                    ?: throw NullPointerException("UID is null.")
            }"
        )

    private val chatChannelsCollectionRef = firestoreInstance.collection("chat_room")
//    private val currentUserDocRef: DocumentReference
//        get() = firestoreInstance.document("/Users/eurPdsswDs3rMG35hqM7") //테스트용

    fun initPatientUser(onComplete: () -> Unit) {
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

    fun initDoctorUser(onComplete: () -> Unit) {
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
    fun updateCurrentUser(name: String = "", bio: String = "") {
        val userFieldMap = mutableMapOf<String, Any>()
        val quserFieldMap = mutableMapOf<String, Any>()
        if (name.isNotBlank()) userFieldMap["name"] = name
        if (bio.isNotBlank()) userFieldMap["bio"] = bio
        userFieldMap["userType"] = true
        quserFieldMap["base_user"] = userFieldMap
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().currentUser!!.uid).update(quserFieldMap)
    }

    fun setPatientUser(health_title: String, health_detail: String) {
        val userFieldMap = mutableMapOf<String, Any>()
        if (health_title.isNotBlank() && health_detail.isNotBlank()) userFieldMap["health_title"] =
            HealthInfo(health_title, health_detail)
        currentUserDocRef.update(userFieldMap)
    }

    fun getDoctorUser(onComplete: (Doctor) -> Unit) {
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

//    fun addUsersListener(context: Context, onListen: (List<Item>) -> Unit): ListenerRegistration {
//        return firestoreInstance.collection("users")
//            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
//                if (firebaseFirestoreException != null) {
//                    Log.e("FIRESTORE", "Users listener error.", firebaseFirestoreException)
//                    return@addSnapshotListener
//                }
//
//                val items = mutableListOf<Item>()
//                querySnapshot!!.documents.forEach {
//                    if (it.id != FirebaseAuth.getInstance().currentUser?.uid)
//                        items.add(PersonItem(it.toObject(User::class.java)!!, it.id, context))
//                }
//                onListen(items)
//            }
//    }

    fun removeListener(registration: ListenerRegistration) = registration.remove()


    fun getOrCreateChatChannel( //채팅방 생성
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
        //onListen: (RecyclerView.ViewHolder) -> Unit
    ): ListenerRegistration {
        Log.d("sad", "sadas")
        var activity = MainActivity()
        activity.Notification()
        return chatChannelsCollectionRef.document(channelId).collection("messages")
            .orderBy("time")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "ChatMessagesListener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }
                val items = mutableListOf<Item>()
                querySnapshot!!.documents.forEach {
                    if (it["type"] == MessageType.TEXT) // 메세지 타입이 텍스트
                        items.add(
                            TextMessageItem(
                                it.toObject(TextMessage::class.java)!!,
                                context!!
                            )
                        )
                    else
                        items.add(
                            ImageMessageItem(it.toObject(ImageMessage::class.java)!!, context!!)
                        )
                    return@forEach
                }
                onListen(items)
            }
    }

    fun sendMessage(message: Message, channelId: String) {
        chatChannelsCollectionRef.document(channelId)
            .collection("messages")
            .add(message)
    }

    fun getFCMRegistrationTokens(onComplete: (tokens: MutableList<String>) -> Unit) {
        currentUserDocRef.get().addOnSuccessListener {
            val user = it["registrationTokens"]
            onComplete(user as MutableList<String>)
        }
    }

    fun setFCMRegistrationTokens(registrationTokens: MutableList<String>) {
        currentUserDocRef.update(mapOf("registrationTokens" to registrationTokens))
    }
}