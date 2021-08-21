package com.example.today_seyebrowktver.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.today_seyebrowktver.R
import com.example.today_seyebrowktver.databinding.FragmentNewcusListBinding
import com.example.today_seyebrowktver.databinding.FragmentRetouchListBinding

class FragmentNewcusList : Fragment() {

    //dataBinding
    private lateinit var binding: FragmentNewcusListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_newcus_list, container, false)
        return binding.root
    }

    companion object {
        fun newInstance(): FragmentNewcusList {
            val args = Bundle()
            val fragment = FragmentNewcusList()
            fragment.arguments = args
            return fragment
        }
    }





}