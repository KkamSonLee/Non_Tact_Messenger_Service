package com.example.non_tact_messenger_service.ViewController

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
import com.example.non_tact_messenger_service.fragment.*
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var suggestionMessage: String
    var userType: Boolean = false       //user Type
    var intent_data: Boolean = false
    var healthTitle: String = ""
    private lateinit var auth: FirebaseAuth
    var otherUID: String = ""           //the other uid
    var fragment_Container =    //iterator pattern(Fragment Handling)
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


        if (intent.hasExtra("user")) {  //User type decide when change from Choice Activity
            if (intent.getBooleanExtra("user", true)) {
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
                .document(FirebaseAuth.getInstance().currentUser!!.uid).get()
                .addOnSuccessListener {    //get current user object
                    var user = it.get("base_user")
                    val objec = user as MutableMap<String, Any>
                    userType = objec["userType"] as Boolean   //userType Save
                    if (userType) {  //when doctor
                        fragmentChange(3)  // to Change Select Fragment
                    } else {  //when patient
                        FirebaseFirestore.getInstance().collection("Users")
                            .document(FirebaseAuth.getInstance().currentUser!!.uid)
                            .collection("chating_room").get().addOnSuccessListener {  //ref chating_room
                                it.forEach {  //get doctor UID
                                    otherUID = it.id
                                }
                                if (intent.hasExtra("noti")) {    //has notification click
                                    fragmentChange(4)     //change to ChatFrament
                                } else {
                                    fragmentChange(7)     //chage to searchFragment
                                }
                            }

                    }
                }
        }

        setContentView(binding.root)
    }


    fun fragmentChange(index: Int) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment_Container[index - 1]).commit()
    }

    fun Notification() {   //Notification Materialization
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
        intent.putExtra("noti", message) //key랑 message
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            2,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        builder.setContentIntent(pendingIntent)

        val manager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager //알림메시지를 NOTIFY
        manager.createNotificationChannel(notificationChannel)
        val notification = builder.build()

        manager.notify(2, notification)
    }
}
