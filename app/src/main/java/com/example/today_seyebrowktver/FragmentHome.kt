package com.example.today_seyebrowktver

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.today_seyebrowktver.databinding.FragmentHomeBinding
import java.util.*

class FragmentHome : Fragment() {

    //viewBinding
    private var _binding: FragmentHomeBinding? = null //onDestory를 위한 변수
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("lifecycle Check", "home onCreate")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):
            View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.menuIv.setOnClickListener(View.OnClickListener {
//            val intent = Intent(context, ActivityHomeMenu::class.java)
//            startActivity(intent)
//
//        })

//        binding.fab.setOnClickListener(View.OnClickListener {
//            (activity as ActivityMain).mSelectTypeOfCustomer()
//
//        })

//        binding.calendarView.setOnDateChangedListener() { view, year, month, dayOfMonth ->
//            val msg = "Selected date is " + dayOfMonth + "/" + (month + 1) + "/" + year
//
//            binding.listTv.text = msg
//            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
//
//        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("lifecycle Check", "home destroy")
    }




}