package com.example.today_seyebrowktver.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.today_seyebrowktver.R
import com.example.today_seyebrowktver.databinding.FragmentCustomersBinding
import com.example.today_seyebrowktver.databinding.FragmentRetouchListBinding
import com.google.android.gms.common.util.DataUtils

class FragmentRetouchList : Fragment() {

    //viewBinding
    private lateinit var binding: FragmentRetouchListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_retouch_list, container, false)
        return binding.root
    }

    companion object {
        fun newInstance(): FragmentRetouchList {
            val args = Bundle()
            val fragment = FragmentRetouchList()
            fragment.arguments = args
            return fragment
        }
    }
}