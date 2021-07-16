package com.example.today_seyebrowktver.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.today_seyebrowktver.databinding.FragmentSalesBinding
import com.example.today_seyebrowktver.viewmodel.FragmentMemoViewModel
import com.example.today_seyebrowktver.viewmodel.FragmentSalesViewModel

private val TAG = "FragmentSales"
class FragmentSales:Fragment() {
    private var _binding: FragmentSalesBinding? = null
    private val binding get() = _binding!!

    //viewModel
    private val fragmentSalesViewModel : FragmentSalesViewModel by lazy {
        ViewModelProvider(this).get(FragmentSalesViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentSalesBinding.inflate(inflater, container, false)
        Log.d(TAG, "onCreateView")

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
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
        Log.d(TAG, "onDestroyView")
        _binding = null
    }

    companion object {
        fun newInstance(): FragmentSales{
            return FragmentSales()
        }
    }
}