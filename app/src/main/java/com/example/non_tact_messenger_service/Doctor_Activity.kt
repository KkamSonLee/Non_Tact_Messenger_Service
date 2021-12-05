package com.example.non_tact_messenger_service

import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.non_tact_messenger_service.databinding.ActivityDoctorBinding
import splitties.toast.toast

class Doctor_Activity : AppCompatActivity() {
    lateinit var binding: ActivityDoctorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }
    fun init(){
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.addJavascriptInterface(WebAppInterface(), "Android")
        binding.webView.loadUrl("https://www.cic.re.kr/symposium/registration/licenseNumberCheck.asp")
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.d("html", "callback!")
                view?.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('table')[0].innerHTML);")
            }
        }
    }
}
