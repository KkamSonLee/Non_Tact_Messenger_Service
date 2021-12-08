package com.example.non_tact_messenger_service.chat

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.non_tact_messenger_service.Firebase_Database
import com.example.non_tact_messenger_service.R
import com.example.non_tact_messenger_service.Storage
import com.example.non_tact_messenger_service.chat.model.ImageMessage
import com.example.non_tact_messenger_service.chat.model.TextMessage
import com.example.non_tact_messenger_service.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_chat.*
import java.io.ByteArrayOutputStream
import java.util.*

private const val RC_SELECT_IMAGE = 2

class ChatFragment :Fragment() {


    private lateinit var currentChannelId: String
    private lateinit var messageListenerRegistration: ListenerRegistration
    private var shouldInitRecyclerView = true // 리싸이클러뷰 구동을 위한 변수
    private lateinit var messagesSection: Section // 그루피 라이브러리를 위한 요소


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        //supportActionBar?.title = intent.getStringExtra(AppConstants.USER_NAME) firebaseauth 사용자가 아닌 다른 사용자 아이디를 이전에 받아와야함
        val otherUserID = "qUnZoyaFHpqbAIWtbf2B" // 임시로 상대방 사용자 id를 넣어줌
        Firebase_Database.getOrCreateChatChannel(otherUserID) { channelId -> // 파이어베이스에서 get하거나 create한 채널 아이디를 통해서 사용함

            currentChannelId = channelId

            messageListenerRegistration =
                Firebase_Database.addChatMessagesListener(channelId, this.activity, this::updateRecyclerView) // observer를 달아줌

            //val sendbtn = view.findViewById<Button>(R.id.btn_sendmsg)

            btn_sendmsg.setOnClickListener { // 전송버튼에 대한 클릭벤트
                val messageToSend = TextMessage(
                    sendinput.text.toString(), Calendar.getInstance().time,
                    "eurPdsswDs3rMG35hqM7", MessageType.TEXT
                ) //FirebaseAuth.getInstance().currentUser!!.uid

                sendinput.setText("") // 입력창을 비운다.
                Firebase_Database.sendMessage(messageToSend, channelId) // 파이어베이스에 메세지를 전송
            }


            btn_sendimg.setOnClickListener {
                val intent = Intent().apply{
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                }
                startActivityForResult(Intent.createChooser(intent, "Select Image"), RC_SELECT_IMAGE)
            }
        }

        return inflater.inflate(R.layout.fragment_chat, container, false)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK &&
            data != null && data.data != null) {
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
                    "eurPdsswDs3rMG35hqM7" // 임시로 사용자 아이디 넣어줌
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
                    add(messagesSection)
                }
            }
            shouldInitRecyclerView = false
        }



        fun updateItems() = messagesSection.update(messages)


        if (shouldInitRecyclerView)
            init()
        else
            updateItems()

        chatrecycler.scrollToPosition(chatrecycler.adapter!!.itemCount - 1)
    }
}