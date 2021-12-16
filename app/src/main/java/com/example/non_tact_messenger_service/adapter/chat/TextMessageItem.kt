package com.example.non_tact_messenger_service.adapter.chat


import android.content.Context

import com.example.non_tact_messenger_service.R
import com.example.non_tact_messenger_service.model.Message.TextMessage
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder


import kotlinx.android.synthetic.main.item_text_message.*

class TextMessageItem (val message: TextMessage, val context: Context)
    : MessageItem(message)

{
    override fun bind(viewHolder: GroupieViewHolder, position: Int) { // adapter for chatFragment RecyclerView
        viewHolder.textView_message_text.text = message.text // ViewHolder for change the text in the xml file
        super.bind(viewHolder, position)
    }
    override fun getLayout() = R.layout.item_text_message  // manage the xml file
}
