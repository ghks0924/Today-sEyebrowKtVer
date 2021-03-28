package com.example.today_seyebrowktver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.today_seyebrowktver.databinding.FragmentMemoBinding
import com.example.today_seyebrowktver.databinding.FragmentMessageBinding

class FragmentMessage:Fragment() {

    private var fragmentMessageBinding: FragmentMemoBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = FragmentMessageBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onDestroyView() {
        fragmentMessageBinding = null
        super.onDestroyView()
    }
}