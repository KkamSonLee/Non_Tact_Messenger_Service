package com.example.non_tact_messenger_service.chat.model

import com.example.non_tact_messenger_service.chat.Message
import java.util.*

data class TextMessage(
    val text: String,
    override val time: Date,
    override val senderId: String,
    override val type: String)
    : Message {

    constructor() : this("", Date(0), "", "")
    }
