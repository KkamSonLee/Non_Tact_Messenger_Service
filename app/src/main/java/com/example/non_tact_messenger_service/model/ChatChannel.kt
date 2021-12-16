package com.example.non_tact_messenger_service.model

data class ChatChannel(val userIds: MutableList<String>) { // data class for chatchannel
    constructor() : this(mutableListOf())
}
