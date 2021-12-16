package com.example.non_tact_messenger_service.model

import com.example.non_tact_messenger_service.chat.model.User

data class Patient(val base_user:User, val healthInfo: MutableList<String>?){
    constructor(): this(User(), null)
    constructor(user:User): this(user, null)
    fun insert_info(data:String){
        this.healthInfo?.add(data)
    }
    fun setHealthInfo(list:MutableList<String>){
        this.healthInfo?.addAll(list)
    }
}