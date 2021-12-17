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
    val scope = CoroutineScope(Dispatchers.IO)//Asynchronized Process variable
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        data = ArrayList<Item_HealthInfo>()
        binding = FragmentSelectBinding.inflate(layoutInflater, container, false)
        adapter = RecyclerviewPatientHealthAdapter(data)   //to connect item from recyclerview adapter
        getHealthInfo()                                    //item initializing

        binding.swipe.setOnRefreshListener {               //refresh data
            binding.swipe.isRefreshing = true
            getHealthInfo()                                //item update
        }
        adapter.itemOnClickListener =
            object : RecyclerviewPatientHealthAdapter.OnItemClickListener {   //recyclerview itemclick event
                override fun OnItemClick(
                    holder: RecyclerView.ViewHolder,
                    view: View,
                    data: Item_HealthInfo,
                    position: Int
                ) {
                    binding_dialog =
                        ContactMessageDialogBinding.inflate(layoutInflater, container, false)
                    val dlg = AlertDialog.Builder(requireContext())     //dialog to suggestion message setting
                    dlg.setView(binding_dialog!!.root)                  //diglog visible
                    dlg.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                        if (binding_dialog.seggestionEdit.text.isNotEmpty()) {               //message not empty
                            (activity as MainActivity).otherUID = data!!.uid.toString()      //patient uid save
                            (activity as MainActivity).suggestionMessage = binding_dialog.seggestionEdit.text.toString()  //doctor seggestion message
                            (activity as MainActivity).fragmentChange(4)     //change to ChatFragment
                        }
                    })
                    dlg.setNegativeButton("취소", null)
                    dlg.show()
                }
            }
        adapter.notifyDataSetChanged()      //adapter item dataSetChanged notify
        binding.patientListview.adapter = adapter    //adapter connect to recyclerview.adapter
        return binding.root
    }


    private fun getHealthInfo() {  //all userHealth get
        scope.launch {           //Asynchronized Start
            FirebaseFirestore.getInstance().collection("Users").get().addOnSuccessListener {  //all of user to get uid
                data.clear()      //data clear
                for (document in it) {   //get User Object
                    FirebaseFirestore.getInstance().collection("Users").document(document.id).get()
                        .addOnSuccessListener {
                            var user = (it.toObject(Patient::class.java)!!)  //to cast patient model
                            if (!(user!!.base_user.userType)) {  //only patient type add HealthInfo
                                data.add(   //add Info
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
                binding.swipe.isRefreshing = false    //refresh finish
                adapter.notifyDataSetChanged()        //adapter item dataSetChanged notify
            }
        }
    }
}