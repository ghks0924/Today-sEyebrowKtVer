package com.example.today_seyebrowktver

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.today_seyebrowktver.databinding.FragmentAccountingBinding

class FragmentAccounting:Fragment() {

    private var _binding: FragmentAccountingBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentAccountingBinding.inflate(inflater, container, false)


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.previousMonthImage.setOnClickListener {

        }

        binding.performanceTv.setOnClickListener {
            val intent = Intent(context, ActivityDetailedPerformance::class.java)
            startActivity(intent)
        }
        binding.nextMonthImage.setOnClickListener {

        }

        binding.monthlyIv.setOnClickListener {
            val intent = Intent(context, ActivityMonthlyPerformance::class.java)
            startActivity(intent)
        }



    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}