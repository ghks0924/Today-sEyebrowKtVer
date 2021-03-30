package com.example.today_seyebrowktver

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.today_seyebrowktver.databinding.ActivityCreateEventSkipCusBinding
import com.example.today_seyebrowktver.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class ActivityMain : ActivityBase() {

    //viewBinding
    private lateinit var binding: ActivityMainBinding

    //fragment
    val fm = supportFragmentManager
    val fmTransaction: FragmentTransaction = fm.beginTransaction()

    //bottomNavigationView만 사용했을 때
//    var fragmentCustomers = FragmentCustomers()
//    var fragmentHome = FragmentHome()
//    var fragmentMemo = FragmentMemo()
//    var fragmentMessage = FragmentMessage()
//    var fragmentAccounting = FragmentAccounting()

    //bottom + viewpager2 아직은 차이 없음
    var fragmentMemo = FragmentMemo()
    var fragmentMessage = FragmentMessage()
    var fragmentHome = FragmentHome()
    var fragmentAccounting = FragmentAccounting()
    var fragmentCustomers = FragmentCustomers()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setFragments()//초기 프래그먼트 생성

        binding.viewpager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.viewpager.registerOnPageChangeCallback(ViewPagerPageChangeCallBack())
        binding.viewpager.offscreenPageLimit = 2
        binding.viewpager.setCurrentItem(2)


    }


    //화면 녹화 테스트입니다.


    fun setFragments() {
        //viewpager 없는버전
//        supportFragmentManager.beginTransaction().add(R.id.frame_container, fragmentCustomers, "customers")
//                .addToBackStack("customers").hide(fragmentCustomers).commit()
//
//        supportFragmentManager.beginTransaction().add(R.id.frame_container, fragmentMemo, "memo")
//                .addToBackStack("memo").hide(fragmentMemo).commit()
//
//        supportFragmentManager.beginTransaction().add(R.id.frame_container, fragmentHome, "home")
//                .addToBackStack("home").commit()
//
//        supportFragmentManager.beginTransaction().add(R.id.frame_container, fragmentAccounting, "accounting")
//                .addToBackStack("accounting").hide(fragmentAccounting).commit()
//
//        supportFragmentManager.beginTransaction().add(R.id.frame_container, fragmentMessage, "message")
//                .addToBackStack("message").hide(fragmentMessage).commit()

//        binding.bottomNavigation.menu.findItem(R.id.nav_home).setChecked(true)


        //바텀 네비게이션뷰 안의 아이템 설정
        binding.bottomNavigation.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener {
            private var menuItem: MenuItem? = null
            override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
                this.menuItem = menuItem
                when (menuItem.itemId) {
                    R.id.nav_memo -> {
//                        if (fragmentMemo == null) {
//                            fragmentMemo = FragmentMemo()
//                            supportFragmentManager.beginTransaction().add(R.id.frame_container, fragmentMemo).commit()
//                        }
//                        if (fragmentMemo != null) {
//                            supportFragmentManager.beginTransaction().show(fragmentMemo).commit()
//                        }
//                        if (fragmentMessage != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentMessage).commit()
//                        }
//                        if (fragmentHome != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentHome).commit()
//                        }
//                        if (fragmentAccounting != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentAccounting).commit()
//                        }
//                        if (fragmentCustomers != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentCustomers).commit()
//                        }
                        binding.viewpager.currentItem = 0
                        return true
                    }
                    R.id.nav_message -> {
//                        if (fragmentMessage == null) {
//                            fragmentMessage = FragmentMessage()
//                            supportFragmentManager.beginTransaction().add(R.id.frame_container, fragmentMessage).commit()
//                        }
//                        if (fragmentMessage != null) {
//                            supportFragmentManager.beginTransaction().show(fragmentMessage).commit()
//                        }
//                        if (fragmentMemo != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentMemo).commit()
//                        }
//                        if (fragmentHome != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentHome).commit()
//                        }
//                        if (fragmentAccounting != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentAccounting).commit()
//                        }
//                        if (fragmentCustomers != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentCustomers).commit()
//                        }
                        binding.viewpager.currentItem = 1
                        return true
                    }
                    R.id.nav_home -> {
//                        if (fragmentHome == null) {
//                            fragmentHome = FragmentHome()
//                            supportFragmentManager.beginTransaction().add(R.id.frame_container, fragmentHome).commit()
//                        }
//                        if (fragmentHome != null) {
//                            supportFragmentManager.beginTransaction().show(fragmentHome).commit()
//                        }
//                        if (fragmentMemo != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentMemo).commit()
//                        }
//                        if (fragmentMessage != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentMessage).commit()
//                        }
//                        if (fragmentAccounting != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentAccounting).commit()
//                        }
//                        if (fragmentCustomers != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentCustomers).commit()
//                        }
                        binding.viewpager.currentItem = 2
                        return true
                    }
                    R.id.nav_accountings -> {
//                        if (fragmentAccounting == null) {
//                            fragmentAccounting = FragmentAccounting()
//                            supportFragmentManager.beginTransaction().add(R.id.frame_container, fragmentAccounting).commit();
//                        }
//                        if (fragmentAccounting != null) {
//                            supportFragmentManager.beginTransaction().show(fragmentAccounting).commit()
//                        }
//                        if (fragmentMemo != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentMemo).commit()
//                        }
//                        if (fragmentMessage != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentMessage).commit()
//                        }
//                        if (fragmentHome != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentHome).commit()
//                        }
//                        if (fragmentCustomers != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentCustomers).commit()
//                        }
                        binding.viewpager.currentItem = 3
                        return true
                    }
                    R.id.nav_customers -> {
//                        if (fragmentCustomers == null) {
//                            fragmentCustomers = FragmentCustomers()
//                            supportFragmentManager.beginTransaction().add(R.id.frame_container, fragmentCustomers).commit()
//                        }
//                        if (fragmentCustomers != null) {
//                            supportFragmentManager.beginTransaction().show(fragmentCustomers).commit()
//                        }
//                        if (fragmentMemo != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentMemo).commit()
//                        }
//                        if (fragmentMessage != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentMessage).commit()
//                        }
//                        if (fragmentAccounting != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentAccounting).commit()
//                        }
//                        if (fragmentHome != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentHome).commit()
//                        }
                        binding.viewpager.currentItem = 4
                        return true
                    }
                }
                return true
            }
        })


    }

    //고객등록 bottomSheetDialog
    fun mSelectHowToCreateCustomer() {
        val frag = BottomSheetFragmentCustomer()
        frag.show(supportFragmentManager, frag.tag)

    }

    //예약등록 bottomSheetDialog
    fun mSelectTypeOfCustomer() {
        val frag = BottomSheetFragmentEvent()
        frag.show(supportFragmentManager, frag.tag)
    }

    private inner class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
        : FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun getItemCount(): Int {
            return 5
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> fragmentMemo
                1 -> fragmentMessage
                2 -> fragmentHome
                3 -> fragmentAccounting
                4 -> fragmentCustomers
                else -> error("no fragment")
            }
        }

    }

    private inner class ViewPagerPageChangeCallBack : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            binding.bottomNavigation.selectedItemId = when (position) {
                0 -> R.id.nav_memo
                1 -> R.id.nav_message
                2 -> R.id.nav_home
                3 -> R.id.nav_accountings
                4 -> R.id.nav_customers
                else -> error("no id")

            }
        }
    }


}

