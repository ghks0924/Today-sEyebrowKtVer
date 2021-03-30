package com.example.today_seyebrowktver

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.today_seyebrowktver.databinding.FragmentHomeBinding
import com.example.today_seyebrowktver.databinding.FragmentMemoBinding

class FragmentMemo:Fragment() {

    private var _binding: FragmentMemoBinding? = null //onDestory를 위한 변수
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("lifecycle Check", "memo onCreateView")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMemoBinding.inflate(inflater, container, false)
        Log.d("lifecycle Check", "memo onCreateView")

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("lifecycle Check", "memo destroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("lifecycle Check", "memo destroy")
    }
}