package com.example.non_tact_messenger_service.model

data class User(
    val name: String,
    val bio: String,
    val userType:Boolean
) {
    constructor() : this("", "", false)
}