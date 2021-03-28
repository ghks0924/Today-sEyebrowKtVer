package com.example.today_seyebrowktver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.today_seyebrowktver.databinding.FragmentHomeBinding
import com.example.today_seyebrowktver.databinding.FragmentMemoBinding

class FragmentMemo:Fragment() {

    private var fragmentMemoBinding: FragmentMemoBinding? = null //onDestory를 위한 변수

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = FragmentMemoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        fragmentMemoBinding = null
        super.onDestroyView()
    }
}