package com.example.non_tact_messenger_service.model

data class HealthInfo(val health_title:String?, val health_detail:String?){
    constructor(): this("","")
}
