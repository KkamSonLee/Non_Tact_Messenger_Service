package com.example.non_tact_messenger_service.model.Message

import java.util.*

data class TextMessage( // Data class for Text Message
    val text: String,
    override val time: Date,
    override val senderId: String,
    override val recipientId: String,
    override val senderName: String,
    override val type: String = MessageType.TEXT
)
    : Message { // implements for Message interface
    constructor() : this("", Date(0), "", "","", "")
    }
