package com.example.non_tact_messenger_service.chat.model

import android.os.IBinder
import com.example.non_tact_messenger_service.chat.Message
import com.example.non_tact_messenger_service.chat.MessageType
import java.util.*

data class ImageMessage(
        val imagepath: String,
    override val time: Date,
    override val senderId: String,
        override val recipientId: String,
        override val senderName: String,
    override val type: String = MessageType.IMAGE)
    : Message {
        constructor() : this("", Date(0),"","", "", "")
    }


