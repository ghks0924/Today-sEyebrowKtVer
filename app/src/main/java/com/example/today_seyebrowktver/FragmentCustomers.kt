package com.example.today_seyebrowktver

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.today_seyebrowktver.databinding.FragmentCustomersBinding

class FragmentCustomers : Fragment() {
    private var fragmentCustomersBinding:FragmentCustomersBinding? = null;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentCustomersBinding.inflate(inflater, container, false)

        binding.abcTv.setOnClickListener(
            View.OnClickListener
            //가나다순 버튼
            {
                binding.abcTv.setTextColor(Color.parseColor("#4f4f4f"))
                binding.savedTv.setTextColor(Color.parseColor("#4D4f4f4f"))
            })

        binding.savedTv.setOnClickListener(View.OnClickListener
        //저장순
        {
            binding.abcTv.setTextColor(Color.parseColor("#4D4f4f4f"))
            binding.savedTv.setTextColor(Color.parseColor("#4f4f4f"))

        })
        return binding.root
    }

    override fun onDestroyView() {
        fragmentCustomersBinding = null;
        super.onDestroyView()
    }
}