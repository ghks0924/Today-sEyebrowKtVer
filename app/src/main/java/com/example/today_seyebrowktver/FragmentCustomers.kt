package com.example.today_seyebrowktver

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.today_seyebrowktver.databinding.FragmentCustomersBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton


class FragmentCustomers : Fragment() {
    private var mBinding:FragmentCustomersBinding? = null //onDestory를 위한 변수

    private val binding get() = mBinding!!

    var data = ArrayList<CustomersData>()
    var adapter: RvCustomerAdapter? = null

    private var mainActivity: MainActivity? = null




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentCustomersBinding.inflate(inflater, container, false)

        setData()
        setRv()

        mainActivity = MainActivity()

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

        binding.fab.setOnClickListener(View.OnClickListener {

            (activity as MainActivity).mSelectHowToCreateCustomer()

        })


        return binding.root
    }

    private fun setData() {
        for (index in 1..5){
            data.add(CustomersData("가나다", "01030445454", "3회"))
        }

    }

    private fun setRv() {
        adapter = RvCustomerAdapter(data)
        binding.recyclerview.setLayoutManager(LinearLayoutManager(context))
        binding.recyclerview.adapter = adapter

        val dividerDecoration: DividerItemDecoration =
            DividerItemDecoration(
                binding.recyclerview.context,
                LinearLayoutManager(context).orientation
            )

        binding.recyclerview.addItemDecoration(dividerDecoration)

//        val decoration_height = RecyclerDecorationHeight(5)
//        binding.recyclerview.addItemDecoration(decoration_height)

    }

    override fun onDestroyView() {
        mBinding = null;
        super.onDestroyView()
    }
}