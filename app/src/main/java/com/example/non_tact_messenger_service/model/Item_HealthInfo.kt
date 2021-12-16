package com.example.non_tact_messenger_service.model

data class Item_HealthInfo(val health_title:String?, val health_detail:String?, val uid:String?){ // Data class for HealthInfo Recyclerview
    constructor(): this("","", "")
}
