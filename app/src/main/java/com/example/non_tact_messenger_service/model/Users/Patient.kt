package com.example.non_tact_messenger_service.model.Users

data class Patient( //Data class for patient
    val base_user: User,
    val registrationTokens: MutableList<String>,
    val health_title: String,
    val health_detail: String
) { //use User data class
    constructor() : this(User(), mutableListOf(), "","")
    constructor(user: User) : this(user, mutableListOf(), "","")

}