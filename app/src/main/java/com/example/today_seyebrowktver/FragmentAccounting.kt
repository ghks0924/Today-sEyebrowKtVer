package com.example.today_seyebrowktver

import android.os.Bundle
import android.util.Log
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
        Log.d("lifecycle Check", "accounting onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentAccountingBinding.inflate(inflater, container, false)

        Log.d("lifecycle Check", "accounting onCreateView")
        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("lifecycle Check", "accounting destroy")
    }
}