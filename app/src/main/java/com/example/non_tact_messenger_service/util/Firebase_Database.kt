package com.example.non_tact_messenger_service.util

import com.example.non_tact_messenger_service.model.UserInfo
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.NullPointerException

object Firebase_Database {
    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document(
            "Users/${
                FirebaseAuth.getInstance().uid
                    ?: throw NullPointerException("UID is null")
            }"
        )

    fun initCurrentUser(onComplete: () -> Unit) {
        currentUserDocRef.get().addOnSuccessListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (!it.exists()) {
                val newUser = UserInfo(
                    user?.displayName ?: "",
                    user?.email ?: "",
                    user?.uid ?: "",
                    null
                )
                currentUserDocRef.set(newUser).addOnSuccessListener {
                    onComplete()
                }
            }else
                onComplete()
        }
    }
    fun updateCurrentUser(name:String = "", email:String = "", uid:String = "", profilePicturePath: String? = null){
        val userFieldMap = mutableMapOf<String, Any>()
        if (name.isNotBlank()) userFieldMap["name"] = name
        if (email.isNotBlank()) userFieldMap["email"] = email
        if (uid.isNotBlank()) userFieldMap["uid"] = uid
        if (profilePicturePath != null) userFieldMap["profilePicturePath"] = profilePicturePath
        currentUserDocRef.update(userFieldMap)
    }
    fun getCurrentUser(onComplete: (User) -> Unit){
        currentUserDocRef.get()
            .addOnSuccessListener {
                onComplete(it.toObject(User::class.java)!!)
            }
    }

}