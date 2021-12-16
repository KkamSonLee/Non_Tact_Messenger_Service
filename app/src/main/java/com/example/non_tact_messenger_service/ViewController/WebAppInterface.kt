package com.example.non_tact_messenger_service.ViewController

import android.util.Log
import android.webkit.JavascriptInterface
import org.jsoup.Jsoup
import java.util.regex.Matcher
import java.util.regex.Pattern


class WebAppInterface{

    @JavascriptInterface
    fun getHtml(html:String){     //Web Response Parsing
        val doc = Jsoup.parse(html)
        val title = doc.select("div[id=data] table").select("a")
        val matcher: Matcher = Pattern    //Regular expression
            .compile("[>](.*?)[<]")
            .matcher(title.toString())
        if(matcher.find()){
            Log.d("result: ", "title= ${matcher.group(1)}")
            val number = matcher.group(1).replace("[^0-9]".toRegex(), "")  //Regular expression
            Log.d("number", number)
            WebCommunication.setLicenseNumber(number)
        }
    }
}