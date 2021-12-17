package com.example.non_tact_messenger_service.model.Message

import java.util.*

data class ImageMessage( // Image Message Data class
        val imagepath: String,
    override val time: Date,
    override val senderId: String,
        override val recipientId: String,
        override val senderName: String,
    override val type: String = MessageType.IMAGE
)
    : Message { // implements for Message interface
        constructor() : this("", Date(0),"","", "", "")
    }


