package com.example.non_tact_messenger_service

import android.content.Context
import android.util.Log
import com.example.non_tact_messenger_service.chat.*
import com.example.non_tact_messenger_service.chat.model.TextMessage
import com.example.non_tact_messenger_service.chat.recycler.TextMessageItem
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.kotlinandroidextensions.Item

object Firebase_Database {

    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

//    private val currentUserDocRef: DocumentReference // 파이어베이스 어센틱케이션을 통해서 해당 파이어스토어의 document를 찾아간다.
//        get() = firestoreInstance.document(
//            "users/${
//                FirebaseAuth.getInstance().currentUser?.uid
//                    ?: throw NullPointerException("UID is null.")
//            }"
//        )

    private val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document("Users/eurPdsswDs3rMG35hqM7") //테스트용

    private val chatChannelsCollectionRef = firestoreInstance.collection("chat_room")


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

//                val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid // 현재 어플 사용자의 auth및 uid를 얻음
                val currentUserId = ("eurPdsswDs3rMG35hqM7") // 테스트용
                val newChannel = chatChannelsCollectionRef.document() //firestore 에 새로운 document(채팅방)추가
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
        onListen: (List<Item>)-> Unit
        //onListen: (RecyclerView.ViewHolder) -> Unit
    ): ListenerRegistration {
        return chatChannelsCollectionRef.document(channelId).collection("messages")
            .orderBy("time")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "ChatMessagesListener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = mutableListOf<Item>()
                querySnapshot!!.documents.forEach {
                    if (it["type"] == MessageType.TEXT)
                        items.add(TextMessageItem(it.toObject(TextMessage::class.java)!!, context!!))
                    else
//                        items.add(
//                            ImageMessageItem(
//                                it.toObject(ImageMessage::class.java)!!,
//                                context
//                            )
//                        )
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
}