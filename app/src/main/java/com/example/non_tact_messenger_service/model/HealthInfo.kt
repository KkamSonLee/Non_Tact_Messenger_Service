package com.example.non_tact_messenger_service.model

data class HealthInfo(val simple_Info:String?, val detail_Info:String?){
    constructor(): this("","")
}
