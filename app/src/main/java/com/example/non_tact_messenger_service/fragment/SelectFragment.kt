package com.example.non_tact_messenger_service.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.non_tact_messenger_service.ViewController.MainActivity
import com.example.non_tact_messenger_service.adapter.healthinfo.RecyclerviewPatientHealthAdapter
import com.example.non_tact_messenger_service.databinding.ContactMessageDialogBinding
import com.example.non_tact_messenger_service.databinding.FragmentSelectBinding
import com.example.non_tact_messenger_service.model.Item_HealthInfo
import com.example.non_tact_messenger_service.model.Users.Patient
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SelectFragment : Fragment() {  //환자 리스트 선택


    lateinit var binding: FragmentSelectBinding
    lateinit var adapter: RecyclerviewPatientHealthAdapter
    lateinit var binding_dialog: ContactMessageDialogBinding
    lateinit var data: ArrayList<Item_HealthInfo>
    val scope = CoroutineScope(Dispatchers.IO)//
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        data = ArrayList<Item_HealthInfo>()
        binding = FragmentSelectBinding.inflate(layoutInflater, container, false)
        adapter = RecyclerviewPatientHealthAdapter(data)
        getHealthInfo()

        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = true
            getHealthInfo()
        }
        adapter.itemOnClickListener =
            object : RecyclerviewPatientHealthAdapter.OnItemClickListener {
                override fun OnItemClick(
                    holder: RecyclerView.ViewHolder,
                    view: View,
                    data: Item_HealthInfo,
                    position: Int
                ) {
                    //Log.d("suggestion", "success")
                    binding_dialog =
                        ContactMessageDialogBinding.inflate(layoutInflater, container, false)
                    val dlg = AlertDialog.Builder(requireContext())
                    dlg.setView(binding_dialog!!.root)
                    dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                        if (binding_dialog.seggestionEdit.text.isNotEmpty()) {
                            (activity as MainActivity).otherUID = data!!.uid.toString()
                            (activity as MainActivity).suggestionMessage = binding_dialog.seggestionEdit.text.toString()
                            (activity as MainActivity).fragmentChange(4)
                            Log.d("suggestion msg",(activity as MainActivity).otherUID)
                        }
                    })
                    dlg.setNegativeButton("취소", null)
                    dlg.show()
                }
            }
        adapter.notifyDataSetChanged()
        binding.patientListview.adapter = adapter
        return binding.root
    }


    private fun getHealthInfo() {
        scope.launch {
            FirebaseFirestore.getInstance().collection("Users").get().addOnSuccessListener {
                data.clear()
                for (document in it) {
                    FirebaseFirestore.getInstance().collection("Users").document(document.id).get()
                        .addOnSuccessListener {
                            var user = (it.toObject(Patient::class.java)!!)
                            if (!(user!!.base_user.userType)) {
                                data.add(
                                    Item_HealthInfo(
                                        user.health_title,
                                        user.health_detail,
                                        document.id
                                    )
                                )
                            }
                        }
                }
            }
            withContext(Dispatchers.Main) {
                binding.swipe.isRefreshing = false
                adapter.notifyDataSetChanged()
            }
        }
    }
}