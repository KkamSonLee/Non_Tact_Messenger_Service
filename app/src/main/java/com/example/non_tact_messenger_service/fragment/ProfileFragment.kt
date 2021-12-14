package com.example.non_tact_messenger_service.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.non_tact_messenger_service.MainActivity
import com.example.non_tact_messenger_service.R
import com.example.non_tact_messenger_service.Storage
import com.example.non_tact_messenger_service.databinding.FragmentProfileBinding
import com.example.non_tact_messenger_service.util.Firebase_Database
import com.firebase.ui.auth.AuthUI
import java.io.ByteArrayOutputStream


class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding

    private val RC_SELECT_IMAGE = 2
    private lateinit var selectedImageBytes: ByteArray
    private var pictureJustChanged = false
    var mainActivity: MainActivity? = null

    // 메인 액티비티 위에 올린다.
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = activity as MainActivity
    }

    // 메인 액티비티에서 내려온다.
    override fun onDetach() {
        super.onDetach()
        mainActivity = null
        binding.editTextName.setText("")
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        return binding.root
        binding.apply {
            imageViewProfilePicture.setOnClickListener {
                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                }
                startActivityForResult(
                    Intent.createChooser(intent, "Select Image"),
                    RC_SELECT_IMAGE
                )
            }
            binding.btnSave.setOnClickListener {
                if (::selectedImageBytes.isInitialized) {
                    Storage.uploadProfilePhoto(selectedImageBytes) { imagePath ->
                        Firebase_Database.updateCurrentUser(
                            binding.editTextName.text.toString(),
                            imagePath
                        )
                        Log.d("image path", imagePath.toString())
                    }

                } else {
                    Firebase_Database.updateCurrentUser(
                        binding.editTextName.text.toString(),
                        null.toString()
                    )

                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectImagePath = data.data
            val selectedImageBmp =
                MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectImagePath)
            val outputStream = ByteArrayOutputStream()
            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            selectedImageBytes = outputStream.toByteArray()
            Log.d("onResult", selectedImageBytes.toString())
            Glide.with(this)
                .load(selectedImageBytes)
                .into(binding.imageViewProfilePicture)
            pictureJustChanged = true
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onStart() {
        super.onStart()
        Log.d("onStart", "buffering")
        Firebase_Database.getCurrentUser { user ->
            if (this@ProfileFragment.isVisible) {
                binding.editTextName.setText(user.name)
                if (!pictureJustChanged && user.profilePicturePath != null) {

                    Glide.with(this)
                        .load(Storage.pathToReference(user.profilePicturePath))
                        .placeholder(R.drawable.fui_ic_check_circle_black_128dp)
                        .into(binding.imageViewProfilePicture)
                } else {
                    Log.d("status", pictureJustChanged.toString())
                    Log.d("status", user.profilePicturePath.toString())
                }
            }
        }
    }
}

