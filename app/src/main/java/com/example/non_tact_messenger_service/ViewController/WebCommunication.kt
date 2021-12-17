package com.example.non_tact_messenger_service.ViewController

import android.util.Log

object WebCommunication {
    private var license_number:String = ""  // lt's for checking existence of licencse

    fun setLicenseNumber(license:String){
        license_number = license
    }
    fun getLicenseNumber():String{
        return license_number
    }
}