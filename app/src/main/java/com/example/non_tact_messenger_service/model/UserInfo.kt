package com.example.non_tact_messenger_service.model

data class UserInfo(val name:String,
val email:String,
val uid:String,
val profilePicturePath:String?){
    constructor(): this("","","", null)
}
