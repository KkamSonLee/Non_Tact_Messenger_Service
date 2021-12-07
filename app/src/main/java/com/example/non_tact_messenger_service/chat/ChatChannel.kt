package com.example.non_tact_messenger_service.chat

data class ChatChannel(val userIds: MutableList<String>) {
    constructor() : this(mutableListOf())
}
