package com.example.non_tact_messenger_service.model.Users

data class Doctor(val base_user: User, var registrationTokens : MutableList<String>, var license:String){ // data class for Users
    constructor(): this(User(), mutableListOf(), "")
    constructor(user: User): this(user, mutableListOf(), "") // use User data class
    fun licenseAccept(license: String){
        this.license = license;
    }
}
