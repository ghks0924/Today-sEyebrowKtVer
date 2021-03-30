package com.example.today_seyebrowktver

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.today_seyebrowktver.databinding.FragmentCustomersBinding


class FragmentCustomers : Fragment() {

    private var _binding: FragmentCustomersBinding? = null //onDestory를 위한 변수

    private val binding get() = _binding!!

    var data = ArrayList<CustomersData>()
    var adapter: RvCustomerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("lifecycle Check", "customers onCreate")
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomersBinding.inflate(inflater, container, false)

        setData() //customer 데이터 받아와서 arrayList에 넣기
        setRv() //recyclerview와 어댑터 세팅

        setLayout() //화면요소 클릭리스너 등 세팅


        Log.d("lifecycle Check", "customers onCreateView")




        return binding.root
    }

    private fun setLayout() {
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

        binding.edittext.clearFocus()

        binding.fab.setOnClickListener(View.OnClickListener {

            (activity as ActivityMain).mSelectHowToCreateCustomer()

        })
    }

    private fun setData() {
        for (index in 1..5) {
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
        super.onDestroyView()
        _binding = null
        Log.d("lifecycle Check", "customers destroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("lifecycle Check", "customers destroy")
    }
}