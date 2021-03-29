package com.example.today_seyebrowktver

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.today_seyebrowktver.databinding.FragmentHomeBinding

class FragmentHome : Fragment() {

    //viewBinding
    private var _binding: FragmentHomeBinding? = null //onDestory를 위한 변수
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):
            View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        binding.menuIv.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, ActivityHomeMenu::class.java)
            startActivity(intent)

        })

        binding.fab.setOnClickListener(View.OnClickListener {
            (activity as ActivityMain).mSelectTypeOfCustomer()
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}