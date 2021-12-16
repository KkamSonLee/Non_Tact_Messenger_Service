package com.example.non_tact_messenger_service.model

data class Doctor(val base_user: User, var registrationTokens : MutableList<String>, var license:String){
    constructor(): this(User(), mutableListOf(), "")
    constructor(user: User): this(user, mutableListOf(), "")
    fun licenseAccept(license: String){
        this.license = license;
    }
}
