package com.example.non_tact_messenger_service.chat

import java.util.*

object MessageType {
    const val TEXT = "TEXT"
    const val IMAGE = "IMAGE"
}

interface Message { //메세지의 인터페이스
    val time: Date
    val senderId: String
    val type: String
}