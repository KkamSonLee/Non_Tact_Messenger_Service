package com.example.non_tact_messenger_service

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.non_tact_messenger_service.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.example.non_tact_messenger_service.chat.ChatFragment
import com.example.non_tact_messenger_service.chat.model.User
import com.example.non_tact_messenger_service.fragment.*
import com.example.non_tact_messenger_service.util.Firebase_Database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import org.json.JSONArray


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var intent_data: Boolean = false
    var healthTitle: String = ""
    var userType: Boolean = false
    private lateinit var auth: FirebaseAuth
    lateinit var otherUID: String
    lateinit var suggestionMessage: String


    var fragment_Container =
        listOf<Fragment>(
            ProfileFragment(),
            SignupFragment(),
            SelectFragment(),
            ChatFragment(),
            DoctorCertifiedFragment(),
            HealthInfoFragment(),
            SearchFragment()
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        auth = Firebase.auth
        if (intent.hasExtra("user")) {
            if (intent.getBooleanExtra("user", false)) {
                fragmentChange(2)
                intent_data = intent.getBooleanExtra("user", false)
                userType = true
            } else {
                fragmentChange(2)
                intent_data = intent.getBooleanExtra("user", false)
                userType = false
            }
        } else {
            FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().currentUser!!.uid).get().addOnSuccessListener {
                var user = it.get("base_user")
                val objec = user as MutableMap<String, Any>
                userType = objec["userType"] as Boolean
                if (userType) {
                    fragmentChange(3)
                } else {
                    fragmentChange(7)
                }
            }
        }
        setContentView(binding.root)
    }
    // 환자 - 구분(Choice Ac) - > 회원가입(Sign Fra) - > 증상입력(SearchFra) -> 건강정보 기입(HealthInfo Fra) -> 연결 대기(Buffering Fra) -> 채팅방(Chat Fra)
    // 환자(로그인 이후) - 증상입력(Search Fra) -> 건강정보 기입 -> 연결 대기 -> 채팅방
    // 의료인 - 구분 -> 회원가입 -> 의료인 인증(DoctorCerti Fra) -> 내 프로필 기입(Profile Fra) -> 환자 건강정보 리스트(Select Fra) -> 연결 대기(Buffering Fra) -> 채팅방(Chat Fra)
    // 의료인(로그인 이후) - 환자 건강정보 리스트 -> 연결 대기 -> 채팅방
    fun Notification() {//--------------------------------------물 섭취 알림부분
        val id = "notichannel"
        val name = "메세지"
        val notificationChannel = NotificationChannel(
            id,
            name,
            NotificationManager.IMPORTANCE_HIGH
        )
        //속성 설정
        notificationChannel.enableVibration(true)//진동
        notificationChannel.enableLights(true) //빛
        notificationChannel.lightColor = Color.BLUE
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        var message = "새로운 메세지가 왔습니다."

        val builder = NotificationCompat.Builder(applicationContext, id)
            .setContentTitle("메세지 알림")
            .setSmallIcon(R.drawable.ic_menu_call)
            .setContentText(message)
            .setAutoCancel(true) //알림 클릭시 삭제 여부

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra("water", message) //key랑 message
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            2,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        builder.setContentIntent(pendingIntent)

        val manager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager //알림메시지를 NOTIFY
        manager.createNotificationChannel(notificationChannel)
        val notification = builder.build()

        manager.notify(2, notification)
    }
    fun fragmentChange(index: Int) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment_Container[index - 1]).commit()
    }

}
