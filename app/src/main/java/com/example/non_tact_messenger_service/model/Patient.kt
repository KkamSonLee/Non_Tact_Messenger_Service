package com.example.non_tact_messenger_service.model

data class Patient(val base_user:UserInfo, val healthInfo: MutableList<String>?){
    constructor(): this(UserInfo(), null)
    constructor(user:UserInfo): this(user, null)
    fun insert_info(data:String){
        this.healthInfo?.add(data)
    }
    fun setHealthInfo(list:MutableList<String>){
        this.healthInfo?.addAll(list)
    }
}
