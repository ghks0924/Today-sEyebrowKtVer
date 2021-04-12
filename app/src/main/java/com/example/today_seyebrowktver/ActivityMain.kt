package com.example.today_seyebrowktver

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.today_seyebrowktver.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ActivityMain : ActivityBase() {

    private val REQUEST_CREATE_MEMO = 1111
    private val REQUEST_UPDATE_MEMO = 2222
    private val REQUEST_SEND_MESSAGE = 3333

    //viewBinding
    private lateinit var binding: ActivityMainBinding

    //viewModel
    private lateinit var mainViewModel: ViewModelMain

    //fragment
    val fm = supportFragmentManager
    val fmTransaction: FragmentTransaction = fm.beginTransaction()


    //bottomNavigationView를 위한 fragments
    var fragmentCustomers = FragmentCustomers()
    var fragmentHome = FragmentHome()
    var fragmentMemo = FragmentMemo()
    var fragmentMessage = FragmentMessage()
    var fragmentAccounting = FragmentAccounting()

    //onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        //뷰모델 생성
        mainViewModel = ViewModelProvider(this)[ViewModelMain::class.java]
        setContentView(view)

        setFragments()//초기 프래그먼트 생성


    }

    //fragment 세팅
    fun setFragments() {
        //viewpager 없는버전
        supportFragmentManager.beginTransaction()
            .add(R.id.frame_container, fragmentCustomers, "customers")
            .addToBackStack("customers").hide(fragmentCustomers).commit()

        supportFragmentManager.beginTransaction().add(R.id.frame_container, fragmentMemo, "memo")
            .addToBackStack("memo").hide(fragmentMemo).commit()

        supportFragmentManager.beginTransaction().add(R.id.frame_container, fragmentHome, "home")
            .addToBackStack("home").commit()

        supportFragmentManager.beginTransaction()
            .add(R.id.frame_container, fragmentAccounting, "accounting")
            .addToBackStack("accounting").hide(fragmentAccounting).commit()

        supportFragmentManager.beginTransaction()
            .add(R.id.frame_container, fragmentMessage, "message")
            .addToBackStack("message").hide(fragmentMessage).commit()

        binding.bottomNavigation.menu.findItem(R.id.nav_home).setChecked(true)

        //바텀 네비게이션뷰 안의 아이템 설정
        binding.bottomNavigation.setOnNavigationItemSelectedListener(object :
            BottomNavigationView.OnNavigationItemSelectedListener {
            private var menuItem: MenuItem? = null
            override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
                this.menuItem = menuItem
                when (menuItem.itemId) {
                    R.id.nav_memo -> {
                        if (fragmentMemo == null) {
                            fragmentMemo = FragmentMemo()
                            supportFragmentManager.beginTransaction()
                                .add(R.id.frame_container, fragmentMemo).commit()
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
                            supportFragmentManager.beginTransaction().hide(fragmentAccounting)
                                .commit()
                        }
                        if (fragmentCustomers != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentCustomers)
                                .commit()
                        }
                        return true
                    }
                    R.id.nav_message -> {
                        if (fragmentMessage == null) {
                            fragmentMessage = FragmentMessage()
                            supportFragmentManager.beginTransaction()
                                .add(R.id.frame_container, fragmentMessage).commit()
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
                            supportFragmentManager.beginTransaction().hide(fragmentAccounting)
                                .commit()
                        }
                        if (fragmentCustomers != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentCustomers)
                                .commit()
                        }
                        return true
                    }
                    R.id.nav_home -> {
                        if (fragmentHome == null) {
                            fragmentHome = FragmentHome()
                            supportFragmentManager.beginTransaction()
                                .add(R.id.frame_container, fragmentHome).commit()
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
                            supportFragmentManager.beginTransaction().hide(fragmentAccounting)
                                .commit()
                        }
                        if (fragmentCustomers != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentCustomers)
                                .commit()
                        }
                        return true
                    }
                    R.id.nav_accountings -> {
                        if (fragmentAccounting == null) {
                            fragmentAccounting = FragmentAccounting()
                            supportFragmentManager.beginTransaction()
                                .add(R.id.frame_container, fragmentAccounting).commit();
                        }
                        if (fragmentAccounting != null) {
                            supportFragmentManager.beginTransaction().show(fragmentAccounting)
                                .commit()
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
                            supportFragmentManager.beginTransaction().hide(fragmentCustomers)
                                .commit()
                        }
                        return true
                    }
                    R.id.nav_customers -> {
                        if (fragmentCustomers == null) {
                            fragmentCustomers = FragmentCustomers()
                            supportFragmentManager.beginTransaction()
                                .add(R.id.frame_container, fragmentCustomers).commit()
                        }
                        if (fragmentCustomers != null) {
                            supportFragmentManager.beginTransaction().show(fragmentCustomers)
                                .commit()
                        }
                        if (fragmentMemo != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentMemo).commit()
                        }
                        if (fragmentMessage != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentMessage).commit()
                        }
                        if (fragmentAccounting != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentAccounting)
                                .commit()
                        }
                        if (fragmentHome != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentHome).commit()
                        }
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

    fun mGoToSendMessageActivity() {

        val intent = Intent(this, ActivitySendMessage::class.java)
        intent.putExtra("content", mainViewModel.messageContent.value)
        startActivityForResult(intent, REQUEST_SEND_MESSAGE)

    }

    fun mGoToCreateMemoActivity() {
        val intent = Intent(this, ActivityCreateMemo::class.java)
        startActivityForResult(intent, REQUEST_CREATE_MEMO)
    }

    fun mGoToUpdateMemoActivity() {
        val intent = Intent(this, ActivityUpdateMemo::class.java)
        intent.putExtra("title", mainViewModel.memoTitle.value)
        intent.putExtra("content", mainViewModel.memoContent.value)
        intent.putExtra("date", mainViewModel.memoDate.value)
        startActivityForResult(intent, REQUEST_UPDATE_MEMO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            return
        }

        when (requestCode) {
            REQUEST_CREATE_MEMO -> {
                //ActivirtCreateMemo에서 넘겨준 데이터 받아오기
                val memoDate = data!!.getStringExtra("date")
                val memoTitle = data!!.getStringExtra("title")
                val memoContent = data!!.getStringExtra("content")

                lifecycleScope.launch(Dispatchers.IO) {
                    mainViewModel.insert(MemoData(memoDate, memoTitle, memoContent))

                }
                mainViewModel.getAll().observe(this, Observer { memos ->
                    var tempMemoDataList = ArrayList<MemoData>()
                    for (i in 0 until memos.size) {
                        tempMemoDataList.add(
                            MemoData(
                                memos[i].memoDate,
                                memos[i].memoTitle,
                                memos[i].memoContent
                            )
                        )
                    }
                })
            }
            REQUEST_UPDATE_MEMO -> {
                val oldMemoDate = data!!.getStringExtra("oldDate")
                val newMemoDate = data!!.getStringExtra("newDate")
                val memoTitle = data!!.getStringExtra("title")
                val memoContent = data!!.getStringExtra("content")

                lifecycleScope.launch(Dispatchers.IO) {
                    mainViewModel.delete(mainViewModel.findMemoByDate(oldMemoDate))
                    mainViewModel.insert(MemoData(newMemoDate, memoTitle, memoContent))
                }
            }
        }
    }

//    //Room Library Methods...
//    fun addMemo(){
//        db?.getMemeDao()?.insert(MemoData2("19:00", "제목", "내용"))
//        for (i in 0 until memoList?.size!!){
//            Log.d("Room Memo Check", "title : " + memoList!![i].memoTitle)
//        }
//    }

}

