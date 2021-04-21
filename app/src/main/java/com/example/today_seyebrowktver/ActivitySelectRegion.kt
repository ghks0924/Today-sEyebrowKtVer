package com.example.today_seyebrowktver

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.today_seyebrowktver.databinding.ActivitySelectRegionBinding


class ActivitySelectRegion : AppCompatActivity() {
    
    //viewBinding
    private lateinit var binding: ActivitySelectRegionBinding
    
    private var data = ArrayList<String>()
    private var adapter: RvRegionAdapter? = null

    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectRegionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dm = applicationContext.resources.displayMetrics
        val width = (dm.widthPixels * 0.9).toInt() // Display 사이즈의 90%
        val height = (dm.heightPixels * 0.9).toInt() // Display 사이즈의 90%
        window.attributes.width = width

        setData()

    }

    private fun setRv() {
        adapter = RvRegionAdapter(data)

        adapter!!.itemClick = object: RvRegionAdapter.ItemClick{
            override fun onClick(view: View, position: Int) {
                var selectedRegion = data.get(position)
                var intent = Intent()
                intent.putExtra("region", selectedRegion)
                setResult(RESULT_OK, intent)
                finish()
            }

        }

        binding.recyclerview.layoutManager = GridLayoutManager(applicationContext, 4)
        binding.recyclerview.adapter = adapter
    }

    private fun setData() {
        data = java.util.ArrayList<String>()
        data.add("서울")
        data.add("경기")
        data.add("인천")
        data.add("부산")
        data.add("대전")
        data.add("대구")
        data.add("울산")
        data.add("광주")
        data.add("세종")
        data.add("제주")
        data.add("강원")
        data.add("충북")
        data.add("충남")
        data.add("전북")
        data.add("전남")
        data.add("경북")
        data.add("경남")

        setRv()

    }
}