package com.example.non_tact_messenger_service.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.non_tact_messenger_service.Firebase_Database
import com.example.non_tact_messenger_service.R
import com.example.non_tact_messenger_service.chat.model.TextMessage
import com.example.non_tact_messenger_service.databinding.FragmentChatBinding
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_chat.*
import java.util.*

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
        val otherUserID = "qUnZoyaFHpqbAIWtbf2B" // 임시로 사용자 id를 넣어줌
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
        }

        return inflater.inflate(R.layout.fragment_chat, container, false)
    }


    private fun updateRecyclerView(messages: List<Item>) {

        fun init() {
            chatrecycler.apply {
                layoutManager = LinearLayoutManager(this@ChatFragment.context) // 리싸이클러뷰 레이아웃 매니저
                adapter = GroupAdapter<GroupieViewHolder>().apply {
                    messagesSection = Section(messages)
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