package com.example.non_tact_messenger_service.model

data class Doctor(val base_user:UserInfo, var is_certified:Boolean){
    constructor(): this(UserInfo(), false)
    constructor(user:UserInfo): this(user, false)
    fun licenseAccept(){
        this.is_certified = true;
    }
}
