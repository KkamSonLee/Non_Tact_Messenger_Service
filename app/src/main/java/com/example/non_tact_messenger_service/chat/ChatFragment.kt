package com.example.non_tact_messenger_service.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.non_tact_messenger_service.Firebase_Database
import com.example.non_tact_messenger_service.databinding.FragmentChatBinding
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.kotlinandroidextensions.Item
import splitties.toast.toast
import android.util.Log
import com.example.non_tact_messenger_service.R

class ChatFragment :Fragment() {

    private lateinit var messageListenerRegistration: ListenerRegistration
    lateinit var binding: FragmentChatBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(layoutInflater)
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        Log.d("asdf","asdfasd")
        //supportActionBar?.title = intent.getStringExtra(AppConstants.USER_NAME) firebaseauth 사용자가 아닌 다른 사용자 아이디를 이전에 받아와야함
            val otherUserID = "qUnZoyaFHpqbAIWtbf2B" // 임시로 사용자 id를 넣어줌
            Firebase_Database.getOrCreateChatChannel(otherUserID){ channelId ->
                messageListenerRegistration =
                    Firebase_Database.addChatMessagesListener(channelId, this.activity, this::onMessagesChanged)
            }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    private fun onMessagesChanged(message: List<Item>){
        toast("onMessageChanged")
    }

}