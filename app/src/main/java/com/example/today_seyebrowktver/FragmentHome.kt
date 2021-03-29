package com.example.today_seyebrowktver

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.today_seyebrowktver.databinding.FragmentHomeBinding

class FragmentHome : Fragment() {
    private var fragmentHomeBinding: FragmentHomeBinding? = null //onDestory를 위한 변수

    init {

    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?):
            View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)


        binding.menuIv.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, HomeMenuActivity::class.java)
            startActivity(intent)

        })

        binding.fab.setOnClickListener(View.OnClickListener {
            (activity as MainActivity).mSelectTypeOfCustomer()
        })

        return binding.root
    }

    override fun onDestroyView() {
        fragmentHomeBinding = null
        super.onDestroyView()
    }
}