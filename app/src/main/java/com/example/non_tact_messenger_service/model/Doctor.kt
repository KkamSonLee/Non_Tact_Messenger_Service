package com.example.non_tact_messenger_service.model

import com.example.non_tact_messenger_service.chat.model.User

data class Doctor(val base_user: User, var license:String){
    constructor(): this(User(), "")
    constructor(user:User): this(user, "")
    fun licenseAccept(license: String){
        this.license = license;
    }
}
