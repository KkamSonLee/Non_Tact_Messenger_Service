package com.example.non_tact_messenger_service.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.non_tact_messenger_service.MainActivity
import com.example.non_tact_messenger_service.R
import com.example.non_tact_messenger_service.RecyclerviewPatientHealthAdapter
import com.example.non_tact_messenger_service.databinding.ContactMessageDialogBinding
import com.example.non_tact_messenger_service.databinding.FragmentSelectBinding
import com.example.non_tact_messenger_service.model.HealthInfo

class SelectFragment : Fragment() {  //환자 리스트 선택


    lateinit var binding: FragmentSelectBinding
    lateinit var adapter: RecyclerviewPatientHealthAdapter
    lateinit var binding_dialog: ContactMessageDialogBinding
    var data = ArrayList<HealthInfo>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectBinding.inflate(layoutInflater, container, false)

        data.add(HealthInfo("sadas", "sadsadsa", ""))
        data.add(HealthInfo("ㅁㄴㅇㅁㄴ", "sadasdasd", ""))
        data.add(HealthInfo("ㄴㅁ", "가나다라", ""))
        data.add(HealthInfo("ㅁㄴㅇㄴㅁㅇ", "마바사", ""))

        adapter = RecyclerviewPatientHealthAdapter(data)
        adapter.itemOnClickListener = object : RecyclerviewPatientHealthAdapter.OnItemClickListener{
            override fun OnItemClick(
                holder: RecyclerView.ViewHolder,
                view: View,
                data: HealthInfo,
                position: Int
            ) {
                //Log.d("suggestion", "success")
                binding_dialog = ContactMessageDialogBinding.inflate(layoutInflater, container, false)
                val dlg = AlertDialog.Builder(requireContext())
                dlg.setView(binding_dialog!!.root)
                dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                    if(binding_dialog.seggestionEdit.text.isNotEmpty()) {
                        Log.d("suggestion msg", binding_dialog.seggestionEdit.text.toString())
                        //(activity as MainActivity).fragmentChange() 버퍼링
                    }
                })
                dlg.setNegativeButton("취소", null)
                dlg.show()
            }

        }
        binding.patientListview.adapter = adapter

        return binding.root
    }
}
