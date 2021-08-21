package com.example.today_seyebrowktver.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.today_seyebrowktver.R
import com.example.today_seyebrowktver.databinding.FragmentNewcusListBinding
import com.example.today_seyebrowktver.databinding.FragmentOldcusListBinding
import com.example.today_seyebrowktver.databinding.FragmentRetouchListBinding

class FragmentOldcusList : Fragment() {

    //viewBinding
    private lateinit var binding: FragmentOldcusListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_oldcus_list, container, false)
        return binding.root
    }

    companion object {
        fun newInstance(): FragmentOldcusList {
            val args = Bundle()
            val fragment = FragmentOldcusList()
            fragment.arguments = args
            return fragment
        }
    }
}