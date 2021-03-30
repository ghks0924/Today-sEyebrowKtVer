package com.example.today_seyebrowktver

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.today_seyebrowktver.databinding.FragmentMemoBinding
import com.example.today_seyebrowktver.databinding.FragmentMessageBinding

class FragmentMessage:Fragment() {

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("lifecycle Check", "message onCreate")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMessageBinding.inflate(inflater, container, false)


        Log.d("lifecycle Check", "message onCreateView")
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("lifecycle Check", "message destroy")
    }

    override fun onDestroy() {
        super.onDestroy()

    }
}