package com.example.non_tact_messenger_service.chat.recycler

import android.content.Context
import com.bumptech.glide.Glide
import com.example.non_tact_messenger_service.R
import com.example.non_tact_messenger_service.Storage
import com.example.non_tact_messenger_service.chat.model.ImageMessage
import com.xwray.groupie.Item
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.item_image_message.*
import kotlinx.android.synthetic.main.item_text_message.*
import com.example.non_tact_messenger_service.glide.GlideModule


class ImageMessageItem(val message: ImageMessage,
                       val context: Context) :MessageItem(message){

    override fun bind(viewHolder: GroupieViewHolder, position: Int) { //리싸이클러뷰에 적용
        super.bind(viewHolder,position)

        Glide.with(context)
            .load(
                Storage.pathToReference(message.imagepath))
            .placeholder(R.drawable.ic_baseline_image_24)
            .into(viewHolder.ImageView_message)

    }

    override fun getLayout() = R.layout.item_image_message

 // 해당 부분들때문에 이미지 전송시에 에러가 남
//    override fun isSameAs(other: Item<*>): Boolean {
//        if (other !is ImageMessageItem)
//            return false
//        if (this.message != other.message)
//            return false
//        return true
//    }
//
//    override fun equals(other: Any?): Boolean {
//        return isSameAs(other as ImageMessageItem)
//    }
//
//    override fun hashCode(): Int {
//        var result = message.hashCode()
//        result = 31 * result + context.hashCode()
//        return result
//    }
}