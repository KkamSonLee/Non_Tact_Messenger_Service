package com.example.non_tact_messenger_service.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.non_tact_messenger_service.ViewController.MainActivity
import com.example.non_tact_messenger_service.ViewController.WebAppInterface
import com.example.non_tact_messenger_service.ViewController.WebCommunication
import com.example.non_tact_messenger_service.databinding.FragmentDoctorCertifiedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DoctorCertifiedFragment : Fragment() {
    lateinit var binding: FragmentDoctorCertifiedBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDoctorCertifiedBinding.inflate(layoutInflater, container, false)
        init()
        return binding.root
    }

    fun init() {    // WebView Attach & Load
        var web = WebAppInterface()
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.addJavascriptInterface(web, "Android")
        binding.webView.loadUrl("https://www.cic.re.kr/symposium/registration/licenseNumberCheck.asp")
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {   //initView
                super.onPageStarted(view, url, favicon)
                view?.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('table')[0].innerHTML);")
            }

            override fun onPageFinished(view: WebView?, url: String?) {     //Requset License Number
                super.onPageFinished(view, url)
                if (WebCommunication.getLicenseNumber() != "") {
                    Log.d("License_Number", WebCommunication.getLicenseNumber())
                    var map = mutableMapOf<String, Any>()
                    map["license"] = WebCommunication.getLicenseNumber()
                    FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().currentUser!!.uid).update(map)
                    (activity as MainActivity).fragmentChange(1)     //change to ProfileFragment
                }
            }
        }
    }
}