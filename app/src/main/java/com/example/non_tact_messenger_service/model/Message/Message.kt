package com.example.non_tact_messenger_service.model.Message

import java.util.*

object MessageType {
    const val TEXT = "TEXT"
    const val IMAGE = "IMAGE"
}

interface Message { //interface for messages
    val time: Date
    val senderId: String
    val recipientId : String
    val senderName : String
    val type: String
}