package com.example.non_tact_messenger_service

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import org.jsoup.Jsoup
import splitties.toast.toast
import java.util.regex.Matcher
import java.util.regex.Pattern


class WebAppInterface(){
    @JavascriptInterface
    fun getHtml(html:String){
        val doc = Jsoup.parse(html)
        val title = doc.select("div[id=data] table").select("a")
        val matcher: Matcher = Pattern
            .compile("[>](.*?)[<]")
            .matcher(title.toString())
        while(matcher.find()){
            Log.d("result: ", "title= ${matcher.group(1)}")
        }
    }
}