package com.example.non_tact_messenger_service.model

import com.example.non_tact_messenger_service.chat.model.User

data class Doctor(val base_user: User, var is_certified:Boolean){
    constructor(): this(User(), false)
    constructor(user:User): this(user, false)
    fun licenseAccept(){
        this.is_certified = true;
    }
}
