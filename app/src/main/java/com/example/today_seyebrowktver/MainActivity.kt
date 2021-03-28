package com.example.today_seyebrowktver

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.today_seyebrowktver.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    //viewBinding
    private var binding: ActivityMainBinding? = null

    //fragment
    val fm = supportFragmentManager
    val fmTransaction: FragmentTransaction = fm.beginTransaction()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFragments()//초기 프래그먼트 생성

//        binding.bottomNavigation.menu.findItem(R.id.nav_home).isChecked = true

//
//        //FrameLayout에 fragment.xml 띄우기
//        binding.bottomNavigation.getMenu().findItem(R.id.nav_home).setChecked(true)
//



    }

    fun setFragments(){
        val fragmentCustomers = FragmentCustomers()
        val fragmentHome = FragmentHome()
        val fragmentMemo = FragmentMemo()

        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragmentCustomers, "customers")
                .addToBackStack("customers").commit()

        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragmentMemo, "memo")
                .addToBackStack("memo").commit()

        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragmentHome, "home")
                .addToBackStack("home").commit()


//        supportFragmentManager.beginTransaction().add(R.id.frame_container, fragmentMemo).commit()
//        supportFragmentManager.beginTransaction().add(R.id.frame_container, fragmentHome).commit()


    }


    override fun onDestroy() {
        // onDestroy 에서 binding class 인스턴스 참조를 정리해주어야 한다.
        binding = null
        super.onDestroy()
    }
}