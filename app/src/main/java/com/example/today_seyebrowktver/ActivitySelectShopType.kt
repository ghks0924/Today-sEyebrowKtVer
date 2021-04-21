package com.example.today_seyebrowktver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.today_seyebrowktver.databinding.ActivitySelectRegionBinding
import com.example.today_seyebrowktver.databinding.ActivitySelectShopTypeBinding

class ActivitySelectShopType : ActivityBase() {

    //viewBinding
    private lateinit var binding: ActivitySelectShopTypeBinding

    private var data = ArrayList<String>()
    private var adapter: RvShopTypeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectShopTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dm = applicationContext.resources.displayMetrics
        val width = (dm.widthPixels * 0.9).toInt() // Display 사이즈의 90%
        val height = (dm.heightPixels * 0.9).toInt() // Display 사이즈의 90%
        window.attributes.width = width

        setData()

    }

    private fun setData() {
        data = java.util.ArrayList<String>()
        data.add("반영구")
        data.add("속눈썹")
        data.add("네일")
        data.add("헤어")
        data.add("기타")


        setRv()

    }

    private fun setRv() {
        adapter = RvShopTypeAdapter(data)

        adapter!!.itemClick = object: RvShopTypeAdapter.ItemClick{
            override fun onClick(view: View, position: Int) {
                var selectedType = data.get(position)
                var intent = Intent()
                intent.putExtra("type", selectedType)
                setResult(RESULT_OK, intent)
                finish()
            }

        }

        binding.recyclerview.layoutManager = GridLayoutManager(applicationContext, 3)
        binding.recyclerview.adapter = adapter
    }
}