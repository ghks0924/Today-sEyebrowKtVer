package com.example.today_seyebrowktver.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.today_seyebrowktver.databinding.ActivityImagePickerBinding
import com.example.today_seyebrowktver.databinding.RvItemPhotoBinding
import com.google.firebase.storage.FirebaseStorage
import gun0912.tedimagepicker.builder.TedImagePicker

class ImagePickerActivity : AppCompatActivity() {

    private var imageUri: Uri? = null
    private var selectedUriList: List<Uri>? = null

    private lateinit var binding: ActivityImagePickerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePickerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onClick(view: View) {
        when (view) {
            binding.btn -> {
//                //singleImage
//                TedImagePicker.with(this)
//                    .start { uri -> showSingleImage(uri) }

                //multiImages
                TedImagePicker.with(this)
                    //.mediaType(MediaType.IMAGE)
                    //.scrollIndicatorDateFormat("YYYYMMDD")
                    //.buttonGravity(ButtonGravity.BOTTOM)
                    //.buttonBackground(R.drawable.btn_sample_done_button)
                    //.buttonTextColor(R.color.sample_yellow)
                    .errorListener { message -> Log.d("ted", "message: $message") }
                    .selectedUri(selectedUriList)
                    .startMultiImage { list: List<Uri> -> showMultiImage(list) }
            }

            binding.uploadBtn ->
                uploadFile()

        }

    }

    private fun showMultiImage(uriList: List<Uri>) {
        this.selectedUriList = uriList
        Log.d("ted", "uriList: $uriList")
//        binding.ivImage.visibility = View.GONE
        binding.containerSelectedPhotos.visibility = View.VISIBLE

        binding.containerSelectedPhotos.removeAllViews()

        val viewSize =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100f, resources.displayMetrics)
                .toInt()
        uriList.forEach {
            val itemImageBinding = RvItemPhotoBinding.inflate(LayoutInflater.from(this))
            Glide.with(this)
                .load(it)
                .apply(RequestOptions().fitCenter())
                .into(itemImageBinding.iv)
            itemImageBinding.root.layoutParams = FrameLayout.LayoutParams(viewSize, viewSize)
            binding.containerSelectedPhotos.addView(itemImageBinding.root)
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