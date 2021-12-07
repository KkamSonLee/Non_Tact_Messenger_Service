package com.example.non_tact_messenger_service.chat.recycler


import android.content.Context
import com.example.non_tact_messenger_service.R
import com.example.non_tact_messenger_service.chat.model.TextMessage
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item

import kotlinx.android.synthetic.main.item_text_message.*

class TextMessageItem (val message: TextMessage, val context: Context)
    : Item()

{
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.message.text = message.text
        //super.bind(viewHolder, position)
    }



    override fun getLayout() = R.layout.item_text_message
}
