package com.example.today_seyebrowktver

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.today_seyebrowktver.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton


class MainActivity : BaseActivity() {

    //viewBinding
    private var mBinding: ActivityMainBinding? = null

    private val binding get() = mBinding!!

    //fragment
    val fm = supportFragmentManager
    val fmTransaction: FragmentTransaction = fm.beginTransaction()

    var fragmentCustomers = FragmentCustomers()
    var fragmentHome = FragmentHome()
    var fragmentMemo = FragmentMemo()
    var fragmentMessage = FragmentMessage()
    var fragmentAccounting = FragmentAccounting()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFragments()//초기 프래그먼트 생성



//        binding.bottomNavigation.menu.findItem(R.id.nav_home).isChecked = true

//
//        //FrameLayout에 fragment.xml 띄우기
//        binding.bottomNavigation.getMenu().findItem(R.id.nav_home).setChecked(true)

    }

    fun setFragments(){

        supportFragmentManager.beginTransaction().add(R.id.frame_container, fragmentCustomers, "customers")
                .addToBackStack("customers").hide(fragmentCustomers).commit()

        supportFragmentManager.beginTransaction().add(R.id.frame_container, fragmentMemo, "memo")
                .addToBackStack("memo").hide(fragmentMemo).commit()

        supportFragmentManager.beginTransaction().add(R.id.frame_container, fragmentHome, "home")
                .addToBackStack("home").commit()

        supportFragmentManager.beginTransaction().add(R.id.frame_container, fragmentAccounting, "accounting")
                .addToBackStack("accounting").hide(fragmentAccounting).commit()

        supportFragmentManager.beginTransaction().add(R.id.frame_container, fragmentMessage, "message")
                .addToBackStack("message").hide(fragmentMessage).commit()

        binding.bottomNavigation.menu.findItem(R.id.nav_home).setChecked(true)


        //바텀 네비게이션뷰 안의 아이템 설정
        binding.bottomNavigation.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener {
            private var menuItem: MenuItem? = null
            override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
                this.menuItem = menuItem
                when (menuItem.itemId) {
                    R.id.nav_memo -> {
                        if (fragmentMemo == null) {
                            fragmentMemo = FragmentMemo()
                            supportFragmentManager.beginTransaction().add(R.id.frame_container, fragmentMemo).commit()
                        }
                        if (fragmentMemo != null) {
                            supportFragmentManager.beginTransaction().show(fragmentMemo).commit()
                        }
                        if (fragmentMessage != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentMessage).commit()
                        }
                        if (fragmentHome != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentHome).commit()
                        }
                        if (fragmentAccounting != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentAccounting).commit()
                        }
                        if (fragmentCustomers != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentCustomers).commit()
                        }
                    }
                    R.id.nav_message -> {
                        if (fragmentMessage == null) {
                            fragmentMessage = FragmentMessage()
                            supportFragmentManager.beginTransaction().add(R.id.frame_container, fragmentMessage).commit()
                        }
                        if (fragmentMessage != null) {
                            supportFragmentManager.beginTransaction().show(fragmentMessage).commit()
                        }
                        if (fragmentMemo != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentMemo).commit()
                        }
                        if (fragmentHome != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentHome).commit()
                        }
                        if (fragmentAccounting != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentAccounting).commit()
                        }
                        if (fragmentCustomers != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentCustomers).commit()
                        }
                    }
                    R.id.nav_home -> {
                        if (fragmentHome == null) {
                            fragmentHome = FragmentHome()
                            supportFragmentManager.beginTransaction().add(R.id.frame_container, fragmentHome).commit()
                        }
                        if (fragmentHome != null) {
                            supportFragmentManager.beginTransaction().show(fragmentHome).commit()
                        }
                        if (fragmentMemo != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentMemo).commit()
                        }
                        if (fragmentMessage != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentMessage).commit()
                        }
                        if (fragmentAccounting != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentAccounting).commit()
                        }
                        if (fragmentCustomers != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentCustomers).commit()
                        }
                    }
                    R.id.nav_accountings -> {
                        if (fragmentAccounting == null) {
                            fragmentAccounting =  FragmentAccounting()
                            supportFragmentManager.beginTransaction().add(R.id.frame_container, fragmentAccounting).commit();
                        }
                        if (fragmentAccounting != null) {
                            supportFragmentManager.beginTransaction().show(fragmentAccounting).commit()
                        }
                        if (fragmentMemo != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentMemo).commit()
                        }
                        if (fragmentMessage != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentMessage).commit()
                        }
                        if (fragmentHome != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentHome).commit()
                        }
                        if (fragmentCustomers != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentCustomers).commit()
                        }
                    }
                    R.id.nav_customers -> {
                        if (fragmentCustomers == null) {
                            fragmentCustomers = FragmentCustomers()
                            supportFragmentManager.beginTransaction().add(R.id.frame_container, fragmentCustomers).commit()
                        }
                        if (fragmentCustomers != null) {
                            supportFragmentManager.beginTransaction().show(fragmentCustomers).commit()
                        }
                        if (fragmentMemo != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentMemo).commit()
                        }
                        if (fragmentMessage != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentMessage).commit()
                        }
                        if (fragmentAccounting != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentAccounting).commit()
                        }
                        if (fragmentHome != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentHome).commit()
                        }
                    }
                }
                return true
            }
        })


    }

    fun mSelectHowToCreateCustomer(){
        mShowShortToast("이건?")

        val frag = BottomSheetFragmentCustomer()
        frag.show(supportFragmentManager, frag.tag)

    }


    override fun onDestroy() {
        // onDestroy 에서 binding class 인스턴스 참조를 정리해주어야 한다.
        mBinding = null
        super.onDestroy()
    }
}

