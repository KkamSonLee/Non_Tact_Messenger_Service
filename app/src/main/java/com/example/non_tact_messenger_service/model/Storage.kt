package com.example.non_tact_messenger_service.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.util.*


object Storage{ // Firebase controller for FireStorage
    //
    private val storageInstance: FirebaseStorage by lazy { FirebaseStorage.getInstance() }

    private val currentUserRef = storageInstance.reference
        .child(FirebaseAuth.getInstance().currentUser?.uid
            ?: throw NullPointerException("UID is null."))


    fun uploadMessageImage(imageBytes: ByteArray,
                           onSuccess: (imagePath: String) -> Unit) {
        val ref = currentUserRef.child("messages/${UUID.nameUUIDFromBytes(imageBytes)}") //store image files in the firestorage
        ref.putBytes(imageBytes)
            .addOnSuccessListener {
                onSuccess(ref.path)
            }
    }

    fun pathToReference(path: String) = storageInstance.getReference(path) // find image path for sending image message
}