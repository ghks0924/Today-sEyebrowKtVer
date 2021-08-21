package com.example.today_seyebrowktver.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.today_seyebrowktver.databinding.ActivityMonthlyCustomerListBinding
import com.google.android.material.tabs.TabLayoutMediator

class ActivityMonthlyCustomerList : FragmentActivity() {

    //viewbinding
    private lateinit var binding: ActivityMonthlyCustomerListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonthlyCustomerListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLayout()

    }

    private fun setLayout() {

        setViewPagerTabLayout()
    }

    private fun setViewPagerTabLayout() {
        val pagerAdapter = PagerFragmentStateAdpater(this)
        // 3개의 fragment add
        pagerAdapter.addFragment(FragmentNewcusList())
        pagerAdapter.addFragment(FragmentOldcusList())
        pagerAdapter.addFragment(FragmentRetouchList())

        //adapter 연결
        binding.viewpager.adapter = pagerAdapter
        binding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.e("ViewPagerFragment", "Page ${position + 1}")
            }
        })

        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->

            when(position){
                0 -> tab.text = "신규고객"
                1 -> tab.text = "기존고객"
                2 -> tab.text = "리터치"
            }

        }.attach()
    }



    //viewPager2를 위한 Adapter
    private inner class PagerFragmentStateAdpater(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        var fragments: ArrayList<Fragment> = ArrayList()

        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }

        fun addFragment(fragment: Fragment){
            fragments.add(fragment)
            notifyItemInserted(fragments.size - 1)
        }

        fun removeFragment(){
            fragments.removeLast()
            notifyItemRemoved(fragments.size)
        }


    }
}