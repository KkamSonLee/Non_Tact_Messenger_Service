package com.example.non_tact_messenger_service.chat.recycler

import android.view.Gravity
import android.widget.FrameLayout
import com.example.non_tact_messenger_service.chat.Message
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_text_message.*
import java.text.SimpleDateFormat

abstract class MessageItem(private val message: Message) :Item()
{
    override fun bind(viewHolder: GroupieViewHolder, position: Int) { //리싸이클러뷰에 적용
        setMessageRootGravity(viewHolder)
        setTimeText(viewHolder)

    }

    private fun setTimeText(viewHolder: GroupieViewHolder){ // 파이어베이스 시간 자료형을 스티링형으로 변환하는 함수
        val dateFormat = SimpleDateFormat
            .getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)
        viewHolder.textView_message_time.text = dateFormat.format(message.time) // 이미지 아이템 레이아웃과 텍스트 아이템 레이아웃의 시간표시 텍스뷰 둘다 아이디가 같음 따라서 한번에 처리
    }


    private fun setMessageRootGravity(viewHolder: GroupieViewHolder) {
        // if (message.senderId == FirebaseAuth.getInstance().currentUser?.uid) { // 내가 보낸 메세지의 경우 오른쪽으로 정렬
        if (message.senderId == "eurPdsswDs3rMG35hqM7") {  //임시로 사용자 아디를 넣어줌
            viewHolder.message_root.apply {
                //background = R.drawable.rect_round_white
                val lParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.END
                )
                this.layoutParams = lParams
            }
        } else { // 다른 사람이 보낸 메세지의 경우 왼쪽으로 정렬
            viewHolder.message_root.apply {
                //backgroundResource = R.drawable.rect_round_primary_color
                val lParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    Gravity.START
                )
                this.layoutParams = lParams
            }
        }
    }
}