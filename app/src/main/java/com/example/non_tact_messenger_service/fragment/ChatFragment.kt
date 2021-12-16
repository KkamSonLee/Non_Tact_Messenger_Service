package com.example.non_tact_messenger_service.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.non_tact_messenger_service.ViewController.MainActivity
import com.example.non_tact_messenger_service.model.Firebase_Database
import com.example.non_tact_messenger_service.R
import com.example.non_tact_messenger_service.model.Storage
import com.example.non_tact_messenger_service.model.MessageType
import com.example.non_tact_messenger_service.model.ImageMessage
import com.example.non_tact_messenger_service.model.TextMessage
import com.example.non_tact_messenger_service.databinding.DoctorProfileDialogBinding
import com.example.non_tact_messenger_service.model.Doctor
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.item_health_info.*
import java.io.ByteArrayOutputStream
import java.util.*

private const val RC_SELECT_IMAGE = 2

class ChatFragment : Fragment() {

    private lateinit var Username: String
    private lateinit var currentChannelId: String // 채널 아이디
    private lateinit var otherUserID: String //채팅방 상대방 아이디
    private lateinit var messageListenerRegistration: ListenerRegistration //FCM 알림을 위한 변수
    private var shouldInitRecyclerView = true // 리싸이클러뷰 구동을 위한 변수
    private lateinit var messagesSection: Section // 그루피 라이브러리를 위한 요소
    lateinit var binding: DoctorProfileDialogBinding
    var mysending: Boolean = false;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (Firebase_Database.is_enabled) {
            sendinput.isEnabled = true
        }

        //supportActionBar?.title = intent.getStringExtra(AppConstants.USER_NAME) firebaseauth 사용자가 아닌 다른 사용자 아이디를 이전에 받아와야함
        otherUserID = (activity as MainActivity).otherUID // 임시로 상대방 사용자 id를 넣어줌

        binding = DoctorProfileDialogBinding.inflate(layoutInflater, container, false)
        if (!(activity as MainActivity).userType) {

            FirebaseFirestore.getInstance().collection("Users").document(otherUserID).get()
                .addOnSuccessListener {
                    var us = it.toObject(Doctor::class.java)!!
                    binding.doctorName.text = us.base_user.name
                    binding.doctorBio.text = us.base_user.bio
                }
        }
        if ((activity as MainActivity).userType) {
            Firebase_Database.getDoctorUser {
                Username = it.base_user.name
            }
        } else {
            Firebase_Database.getPatientUser {
                Username = it.base_user.name
            }
        }


        Firebase_Database.getOrCreateChatChannel(otherUserID) { channelId -> // 파이어베이스에서 get하거나 create한 채널 아이디를 통해서 사용함

            currentChannelId = channelId
            if ((activity as MainActivity).userType) {
                val suggestionMessage = TextMessage(
                    (activity as MainActivity).suggestionMessage, Calendar.getInstance().time,
                    FirebaseAuth.getInstance().currentUser!!.uid, otherUserID, "",
                    MessageType.TEXT
                )
                Firebase_Database.sendMessage(suggestionMessage, channelId)
            }
            messageListenerRegistration =
                Firebase_Database.addChatMessagesListener(
                    channelId,
                    this.activity,
                    this::updateRecyclerView
                )
            hire_btn.setOnClickListener {
                Firebase_Database.is_enabled = true
                sendinput.isEnabled = true
                val messageToSend = TextMessage(
                    "고용되었습니다.", Calendar.getInstance().time,
                    FirebaseAuth.getInstance().currentUser!!.uid,
                    otherUserID, Username,
                    MessageType.TEXT
                ) //FirebaseAuth.getInstance().currentUser!!.uid
                Firebase_Database.sendMessage(messageToSend, channelId)
                Toast.makeText(context, "고용되었습니다!", Toast.LENGTH_SHORT).show()
            }
            if ((activity as MainActivity).userType) {
                hire_btn.visibility = View.INVISIBLE
                profile_btn.visibility = View.INVISIBLE
            } else {
                hire_btn.visibility = View.VISIBLE
                profile_btn.visibility = View.VISIBLE
            }
            profile_btn.setOnClickListener {
                val dlg = AlertDialog.Builder(requireContext())
                dlg.setView(binding!!.root)
                dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                })
                dlg.setNegativeButton("취소", null)
                dlg.show()
            }
            btn_sendmsg.setOnClickListener { // 전송버튼에 대한 클릭벤트
                val messageToSend = TextMessage(
                    sendinput.text.toString(), Calendar.getInstance().time,
                    FirebaseAuth.getInstance().currentUser!!.uid,
                    otherUserID, Username,
                    MessageType.TEXT
                ) //FirebaseAuth.getInstance().currentUser!!.uid
                mysending = true
                sendinput.setText("") // 입력창을 비운다.
                Firebase_Database.sendMessage(messageToSend, channelId) // 파이어베이스에 메세지를 전송
            }


            btn_sendimg.setOnClickListener {
                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                }
                mysending = true
                startActivityForResult(
                    Intent.createChooser(intent, "Select Image"),
                    RC_SELECT_IMAGE
                )
            }
        }

        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK &&
            data != null && data.data != null
        ) {
            val selectedImagePath = data.data

            val selectedImageBmp =
                MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectedImagePath)

            val outputStream = ByteArrayOutputStream()

            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            val selectedImageBytes = outputStream.toByteArray()

            Storage.uploadMessageImage(selectedImageBytes) { imagePath ->

//                    ImageMessage(imagePath, Calendar.getInstance().time,
//                        FirebaseAuth.getInstance().currentUser!!.uid,
//                        otherUserId, currentUser.name)
                val messageToSend = ImageMessage(
                    imagePath, Calendar.getInstance().time,
                    FirebaseAuth.getInstance().currentUser!!.uid,
                    otherUserID, Username,
                    MessageType.IMAGE// 임시로 사용자 아이디 넣어줌
                )
                Firebase_Database.sendMessage(messageToSend, currentChannelId)
            }
        }
    }

    private fun updateRecyclerView(messages: List<Item>) { // 해당 함수에서 리싸이클러뷰의 레이아웃 매니저설정과 어댑터를 달아줌

        fun init() {
            chatrecycler.apply {
                layoutManager = LinearLayoutManager(this@ChatFragment.context) // 리싸이클러뷰 레이아웃 매니저
                adapter = GroupAdapter<GroupieViewHolder>().apply {
                    messagesSection = Section(messages) // 어댑터에 아이템들을 넣어줌
                    if (!Firebase_Database.is_enabled) {
                        if (messagesSection.itemCount>= 2) {
                            Firebase_Database.is_enabled = true
                            sendinput.isEnabled = true
                        }
                    }
                    add(messagesSection)
                }
            }
            shouldInitRecyclerView = false
        }

        fun updateItems() = messagesSection.update(messages)
        if (shouldInitRecyclerView)
            init()
        else {
            updateItems()
            if(!mysending) {
                (activity as MainActivity).Notification()
            }else{
                mysending = false
            }
        }
        chatrecycler.scrollToPosition(chatrecycler.adapter!!.itemCount - 1)
    }
}