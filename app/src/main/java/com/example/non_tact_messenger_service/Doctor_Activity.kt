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
import org.jsoup.Jsoup
import splitties.toast.toast
import java.util.regex.Matcher
import java.util.regex.Pattern

class Doctor_Activity : AppCompatActivity() {
    lateinit var binding: ActivityDoctorBinding
    var isComplete = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    fun init() {
        var web = WebAppInterface()

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.addJavascriptInterface(web, "Android")
        binding.webView.loadUrl("https://www.cic.re.kr/symposium/registration/licenseNumberCheck.asp")
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                view?.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('table')[0].innerHTML);")
                var is_cv = web.getHtml(view?.url.toString())
                //Log.d("ddd", is_cv.toString())
            }
        }
    }

}
