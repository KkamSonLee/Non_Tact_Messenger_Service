package com.example.non_tact_messenger_service.model

import com.example.non_tact_messenger_service.chat.model.User

data class Patient(
    val base_user: User,
    val registrationTokens: MutableList<String>,
    val health_title: String,
    val health_detail: String
) {
    constructor() : this(User(), mutableListOf(), "","")
    constructor(user: User) : this(user, mutableListOf(), "","")

}