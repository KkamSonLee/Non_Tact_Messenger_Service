package com.example.non_tact_messenger_service.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentHandler (fragmentActivity: FragmentActivity, private val userId: String) :
    FragmentStateAdapter(fragmentActivity) {



    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {  //뷰페이저 페이지 전환
        return when (position) {
            else -> return Fragment()
        }
    }
}