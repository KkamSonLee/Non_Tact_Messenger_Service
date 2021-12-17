package com.example.non_tact_messenger_service.model.Users

data class User( // data class for User
    val name: String,
    val bio: String,
    val userType:Boolean
) {
    constructor() : this("", "", false)
}