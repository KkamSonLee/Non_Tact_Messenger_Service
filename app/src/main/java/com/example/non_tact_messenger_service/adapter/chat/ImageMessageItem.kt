package com.example.non_tact_messenger_service.adapter.chat

import android.content.Context
import com.bumptech.glide.Glide
import com.example.non_tact_messenger_service.R
import com.example.non_tact_messenger_service.model.Storage
import com.example.non_tact_messenger_service.model.Message.ImageMessage
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.item_image_message.*


class ImageMessageItem(val message: ImageMessage,
                       val context: Context) : MessageItem(message){

    override fun bind(viewHolder: GroupieViewHolder, position: Int) { // adapter for chatFragment Recycler View
        super.bind(viewHolder,position) // ViewHolder for change the text in the xml file

        Glide.with(context) // manage the images in device
            .load(
                Storage.pathToReference(message.imagepath))
            .placeholder(R.drawable.ic_baseline_image_24)
            .into(viewHolder.ImageView_message)

    }

    override fun getLayout() = R.layout.item_image_message // manage the xml file

}