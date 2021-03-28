package com.example.today_seyebrowktver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.today_seyebrowktver.databinding.FragmentAccountingBinding

class FragmentAccounting:Fragment() {

    private var fragmentAccountingBinding: FragmentAccountingBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = FragmentAccountingBinding.inflate(inflater, container, false)


        return binding.root

    }

    override fun onDestroyView() {
        fragmentAccountingBinding = null
        super.onDestroyView()
    }
}