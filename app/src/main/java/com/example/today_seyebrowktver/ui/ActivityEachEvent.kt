package com.example.today_seyebrowktver.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.today_seyebrowktver.data.PhotoData
import com.example.today_seyebrowktver.RvPhotoAdapter
import com.example.today_seyebrowktver.databinding.ActivityEachEventBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ActivityEachEvent : ActivityBase() {

    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var uid = mAuth.currentUser!!.uid

    //RecyclerView를 위한 변수들
    private var photoDataList = ArrayList<PhotoData>()
    private var adapter: RvPhotoAdapter? = null

    private lateinit var binding:ActivityEachEventBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEachEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //팝업 액티비티의 크기 조절
        val dm = applicationContext.resources.displayMetrics
        val width = (dm.widthPixels * 0.9).toInt() // Display 사이즈의 90%
        val height = (dm.widthPixels * 1.5).toInt()
        window.attributes.width = width
        window.attributes.height = height


        mSetPhotoData()

    }

    private fun mSetPhotoData() {
        //서버에서 섬네일이랑 실제 경로 구해오기

        //Test code

        for (i in 0 until 10){
            photoDataList.add(PhotoData("www.naver.com"))
        }

        setRv()

    }

    private fun setRv() {
        adapter = RvPhotoAdapter(photoDataList)

        //itemClick event
        adapter!!.itemClick = object : RvPhotoAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                mShowShortToast("클릭")
            }
        }

        //itemLongClick event
        adapter!!.itemLongClick = object : RvPhotoAdapter.ItemLongClick {
            override fun onLongClick(view: View, position: Int) {
            }
        }

        binding.photoRv.layoutManager = GridLayoutManager(applicationContext,4)
        binding.photoRv.adapter = adapter
    }
}