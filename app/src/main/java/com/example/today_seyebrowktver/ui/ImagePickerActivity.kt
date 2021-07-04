package com.example.today_seyebrowktver.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.today_seyebrowktver.databinding.ActivityImagePickerBinding
import com.google.firebase.storage.FirebaseStorage
import gun0912.tedimagepicker.builder.TedImagePicker

class ImagePickerActivity : AppCompatActivity() {

    private var imageUri: Uri?= null

    private lateinit var binding:ActivityImagePickerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePickerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onClick(view: View) {
        when (view){
            binding.btn ->
                TedImagePicker.with(this)
                .start { uri -> showSingleImage(uri) }

            binding.uploadBtn ->
                uploadFile()
        }

    }

    private fun uploadFile() {
        var file = imageUri
        val storageRef = FirebaseStorage.getInstance().reference
        val riversRef = storageRef.child("images/uid/test.jpg")
        val uploadTask = riversRef.putFile(file!!)

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener { taskSnapshot ->
            Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showSingleImage(uri: Uri) {

        imageUri = uri
        Glide.with(this).load(uri).into(binding.iv)

    }
}