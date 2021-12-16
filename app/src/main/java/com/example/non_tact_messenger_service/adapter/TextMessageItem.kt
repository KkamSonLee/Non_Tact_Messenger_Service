package com.example.non_tact_messenger_service.adapter


import android.content.Context

import com.example.non_tact_messenger_service.R
import com.example.non_tact_messenger_service.model.TextMessage
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder


import kotlinx.android.synthetic.main.item_text_message.*

class TextMessageItem (val message: TextMessage, val context: Context)
    : MessageItem(message)

{
    override fun bind(viewHolder: GroupieViewHolder, position: Int) { //리싸이클러뷰에 적용
        viewHolder.textView_message_text.text = message.text // 뷰홀더를 통해서 레이아웃의 텍스뷰의 텍스트를 메세지 내용으로 바꿈
        super.bind(viewHolder, position)
    }


    override fun getLayout() = R.layout.item_text_message  // 레이아웃 설정

    
     // 현재 얘네들 쓰게 된다면 정렬에 문제가 생김 우선 주석 처리
//    override fun isSameAs(other: Item<*>): Boolean {
//        if (other !is TextMessageItem)
//            return false
//        if (this.message != other.message)
//            return false
//        return true
//    }
//
//    override fun equals(other: Any?): Boolean {
//        return isSameAs(other as TextMessageItem)
//    }
//
//    override fun hashCode(): Int {
//        var result = message.hashCode()
//        result = 31 * result + context.hashCode()
//        return result
//    }

}
