package com.example.today_seyebrowktver.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import com.example.today_seyebrowktver.*
import com.example.today_seyebrowktver.data.MemoData
import com.example.today_seyebrowktver.data.MessageData
import com.example.today_seyebrowktver.databinding.ActivityMainBinding
import com.example.today_seyebrowktver.viewmodel.ActivityMainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "ActivityMain"

private const val FRAGMENT_TAG_MEMO = "FragmentMemo"

private const val FRAGMENT_TAG_MESSAGE = "FragmentMessage"
private const val FRAGMENT_TAG_CALENDAR = "FragmentCalendar"
private const val FRAGMENT_TAG_SALES = "FragmentSales"
private const val FRAGMENT_TAG_CUSTOMER = "FragmentCustomer"

class ActivityMain : ActivityBase() {

    private val REQUEST_CREATE_MEMO = 11112
    private val REQUEST_UPDATE_MEMO = 2222
    private val REQUEST_SEND_MESSAGE = 3333

    //viewBinding
    internal lateinit var binding: ActivityMainBinding

    //viewModel
    private val activityMainViewModel: ActivityMainViewModel by lazy {
        ViewModelProvider(this).get(ActivityMainViewModel::class.java)
    }

    //bottomNavigationView를 위한 fragments
    private var fragmentCustomers: FragmentCustomers? = null
    private var fragmentMemo: FragmentMemo? = null
    private var fragmentMessage: FragmentMessage? = null
    private var fragmentSales: FragmentSales? = null
    private var fragmentCalendar: Example5Fragment? = null

    private lateinit var lastSelectedFragment: Fragment

    private lateinit var getResultActivty: ActivityResultLauncher<Intent>

    val activityForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            Log.d("intentTag", "new way is working")
        }
    }


    //onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setPermittionCheck() //권한체크

        Log.d(TAG, "onCreate")

        getResultActivty = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val mString = result.data?.getStringExtra("key")
                Log.d(TAG, "goot To go${mString}")
            }
        }


        /**
         * navigationView revise
         */


        setContentView(view)
        initFragments()
        initNavigationBar()
        setStatusBarColor(this, R.color.white)


    }

    override fun onStart() {
        super.onStart()
    }

    //BottomNavigationView 관련
    private fun initNavigationBar() {
        //fragment hide&&show를 위해 일단 다 생성하고 숨김


        Log.d(TAG, "call initNavigationBar()")

        //fragment hide&&show를 위해 일단 다 생성하고 숨김
        supportFragmentManager.beginTransaction()
            .add(R.id.frame_container, fragmentMemo!!, FRAGMENT_TAG_MEMO).hide(fragmentMemo!!).commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.frame_container, fragmentMessage!!, FRAGMENT_TAG_MESSAGE).hide(fragmentMessage!!)
            .commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.frame_container, fragmentCalendar!!, FRAGMENT_TAG_CALENDAR).show(fragmentCalendar!!)
            .commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.frame_container, fragmentSales!!, FRAGMENT_TAG_SALES).hide(fragmentSales!!)
            .commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.frame_container, fragmentCustomers!!, FRAGMENT_TAG_CUSTOMER).hide(fragmentCustomers!!)
            .commit()

        //ItemSelectedListener 세팅
        binding.bottomNavigation.run {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {

                    R.id.nav_memo -> changeFragment(fragmentMemo!!)
                    R.id.nav_message -> changeFragment(fragmentMessage!!)
                    R.id.nav_home -> changeFragment(fragmentCalendar!!)
                    R.id.nav_sales -> changeFragment(fragmentSales!!)
                    R.id.nav_customers -> changeFragment(fragmentCustomers!!)
                }
                true
            }
            selectedItemId = R.id.nav_home //처음에 fragmentCalendar로 clicked 세팅
        }

    }

    private fun initFragments() {
        if (fragmentCalendar == null){
            //fragment 생성
            fragmentMemo = FragmentMemo.newInstance()
            fragmentMessage = FragmentMessage.newInstance()
            fragmentCalendar = Example5Fragment.newInstance()
            fragmentSales = FragmentSales.newInstance()
            fragmentCustomers = FragmentCustomers.newInstance()

            lastSelectedFragment = fragmentCalendar!!
        }

    }

    private fun changeFragment(showFragment: Fragment) {
        //이전 fragment hide
        supportFragmentManager.beginTransaction().hide(lastSelectedFragment).commit()
        //클릭된 fragment show
        supportFragmentManager.beginTransaction().show(showFragment).commit()
        //클릭된 fragment 저장
        lastSelectedFragment = showFragment

    }


//fragment 세팅
//    fun setFragments() {
//        //viewpager 없는버전
//        supportFragmentManager.beginTransaction()
//            .add(R.id.frame_container, fragmentCustomers, "customers")
//            .addToBackStack("customers").hide(fragmentCustomers).commit()
//
//        supportFragmentManager.beginTransaction().add(R.id.frame_container, fragmentMemo, "memo")
//            .addToBackStack("memo").hide(fragmentMemo).commit()
//
//        supportFragmentManager.beginTransaction()
//            .add(R.id.frame_container, fragmentCalendar, "home")
//            .addToBackStack("home").commit()
//
//        supportFragmentManager.beginTransaction()
//            .add(R.id.frame_container, fragmentSales, "accounting")
//            .addToBackStack("accounting").hide(fragmentSales).commit()
//
//        supportFragmentManager.beginTransaction()
//            .add(R.id.frame_container, fragmentMessage, "message")
//            .addToBackStack("message").hide(fragmentMessage).commit()
//
//        binding.bottomNavigation.menu.findItem(R.id.nav_home).setChecked(true)
//
//        //바텀 네비게이션뷰 안의 아이템 설정
//        binding.bottomNavigation.setOnNavigationItemSelectedListener(object :
//            BottomNavigationView.OnNavigationItemSelectedListener {
//            private var menuItem: MenuItem? = null
//            override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
//                this.menuItem = menuItem
//                when (menuItem.itemId) {
//                    R.id.nav_memo -> {
//                        if (fragmentMemo == null) {
//                            fragmentMemo = FragmentMemo()
//                            supportFragmentManager.beginTransaction()
//                                .add(R.id.frame_container, fragmentMemo).commit()
//                        }
//                        if (fragmentMemo != null) {
//                            supportFragmentManager.beginTransaction().show(fragmentMemo).commit()
//                        }
//                        if (fragmentMessage != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentMessage).commit()
//                        }
//                        if (fragmentCalendar != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentCalendar)
//                                .commit()
//                        }
//                        if (fragmentSales != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentSales)
//                                .commit()
//                        }
//                        if (fragmentCustomers != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentCustomers)
//                                .commit()
//                        }
//                        return true
//                    }
//                    R.id.nav_message -> {
//                        if (fragmentMessage == null) {
//                            fragmentMessage = FragmentMessage()
//                            supportFragmentManager.beginTransaction()
//                                .add(R.id.frame_container, fragmentMessage).commit()
//                        }
//                        if (fragmentMessage != null) {
//                            supportFragmentManager.beginTransaction().show(fragmentMessage).commit()
//                        }
//                        if (fragmentMemo != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentMemo).commit()
//                        }
//                        if (fragmentCalendar != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentCalendar)
//                                .commit()
//                        }
//                        if (fragmentSales != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentSales)
//                                .commit()
//                        }
//                        if (fragmentCustomers != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentCustomers)
//                                .commit()
//                        }
//                        return true
//                    }
//                    R.id.nav_home -> {
//                        if (fragmentCalendar == null) {
//                            fragmentCalendar = Example5Fragment()
//                            supportFragmentManager.beginTransaction()
//                                .add(R.id.frame_container, fragmentCalendar).commit()
//                        }
//                        if (fragmentCalendar != null) {
//                            supportFragmentManager.beginTransaction().show(fragmentCalendar)
//                                .commit()
//                        }
//                        if (fragmentMemo != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentMemo).commit()
//                        }
//                        if (fragmentMessage != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentMessage).commit()
//                        }
//                        if (fragmentSales != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentSales)
//                                .commit()
//                        }
//                        if (fragmentCustomers != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentCustomers)
//                                .commit()
//                        }
//                        return true
//                    }
//                    R.id.nav_sales -> {
//                        if (fragmentSales == null) {
//                            fragmentSales = FragmentSales()
//                            supportFragmentManager.beginTransaction()
//                                .add(R.id.frame_container, fragmentSales).commit();
//                        }
//                        if (fragmentSales != null) {
//                            supportFragmentManager.beginTransaction().show(fragmentSales)
//                                .commit()
//                        }
//                        if (fragmentMemo != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentMemo).commit()
//                        }
//                        if (fragmentMessage != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentMessage).commit()
//                        }
//                        if (fragmentCalendar != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentCalendar)
//                                .commit()
//                        }
//                        if (fragmentCustomers != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentCustomers)
//                                .commit()
//                        }
//                        return true
//                    }
//                    R.id.nav_customers -> {
//                        if (fragmentCustomers == null) {
//                            fragmentCustomers = FragmentCustomers()
//                            supportFragmentManager.beginTransaction()
//                                .add(R.id.frame_container, fragmentCustomers).commit()
//                        }
//                        if (fragmentCustomers != null) {
//                            supportFragmentManager.beginTransaction().show(fragmentCustomers)
//                                .commit()
//                        }
//                        if (fragmentMemo != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentMemo).commit()
//                        }
//                        if (fragmentMessage != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentMessage).commit()
//                        }
//                        if (fragmentSales != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentSales)
//                                .commit()
//                        }
//                        if (fragmentCalendar != null) {
//                            supportFragmentManager.beginTransaction().hide(fragmentCalendar)
//                                .commit()
//                        }
//                        return true
//                    }
//                }
//                return true
//            }
//        })
//
//
//    }

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
//        intent.putExtra("type", mainViewModel.messageType.value)
//        intent.putExtra("title", mainViewModel.messageTitle.value)
//        intent.putExtra("content", mainViewModel.messageContent.value)
//        intent.putExtra("date", mainViewModel.messageDate.value)
        startActivityForResult(intent, REQUEST_SEND_MESSAGE)

    }

    fun mGoToCreateMemoActivity() {
//        val intent = Intent(this, ActivityCreateMemo::class.java)
//        startActivityForResult(intent, REQUEST_CREATE_MEMO)

        val mIntent = Intent(this@ActivityMain, ActivityCreateMemo::class.java)
        getResultActivty.launch(mIntent)
//
//        activityForResult.launch(
//            Intent(this,
//                ActivityCreateMemo::class.java))
    }


    fun mGoToUpdateMemoActivity() {
        activityForResult.launch(
            Intent(this,
                ActivityUpdateMemo::class.java))
    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode != RESULT_OK) {
//            return
//        }
//        Log.d(TAG, "requestCode" + requestCode)
//
//        Log.d(TAG, "CreateMemo" + requestCode)
//        when (requestCode) {
//            REQUEST_CREATE_MEMO -> {
//                Log.d(TAG, "CreateMemo" + REQUEST_CREATE_MEMO)
//                Log.d(TAG, data?.getStringExtra("content").toString())
//                Log.d(TAG, data!!.getStringExtra("date").toString())
//                Log.d(TAG, data!!.getStringExtra("id").toString())
//
//                //ActivirtCreateMemo에서 넘겨준 데이터 받아오기
//                val memo = MemoData(data?.getStringExtra("title").toString(),
//                    data?.getStringExtra("content").toString(),
//                    data!!.getStringExtra("date").toString(),
//                    data!!.getStringExtra("id").toString())
//
//                Log.d("memoCheck", "id" + memo.memoId)
//
//                lifecycleScope.launch(Dispatchers.IO) {
////                        mainViewModel.insert(memo)
//                    Log.d("memoCheck", "id" + memo.memoId)
//                }
////                mainViewModel.getAll().observe(this, Observer { memos ->
////                    var tempMemoDataList = ArrayList<MemoData>()
////                    for (i in 0 until memos.size) {
////                        tempMemoDataList.add(
////                            MemoData(
////                                memos[i].memoDate,
////                                memos[i].memoTitle,
////                                memos[i].memoContent
////                            )
////                        )
////                    }
////                })
//            }
//            REQUEST_UPDATE_MEMO -> {
////                val oldMemoDate = data!!.getStringExtra("oldDate")
////                val newMemoDate = data!!.getStringExtra("newDate")
////                val memoTitle = data!!.getStringExtra("title")
////                val memoContent = data!!.getStringExtra("content")
////
////                Log.d("memoCheck", "fragment : " + newMemoDate)
////
////                lifecycleScope.launch(Dispatchers.IO) {
////                    mainViewModel.delete(mainViewModel.findMemoByDate(oldMemoDate.toString()))
////                    mainViewModel.insert(MemoData(newMemoDate.toString(), memoTitle.toString(), memoContent.toString()))
////                }
//            }
//
//            REQUEST_SEND_MESSAGE -> {
//                val messageType = data!!.getStringExtra("type")
//                val oldMessageDate = data!!.getStringExtra("oldDate")
//                val newMessageDate = data!!.getStringExtra("newDate")
//                val newMessageTitle = data!!.getStringExtra("title")
//                val newMessageContent = data!!.getStringExtra("content")
//                lifecycleScope.launch(Dispatchers.IO) {
////                        mainViewModel.delete(mainViewModel.findMessageByDate(oldMessageDate.toString()))
////                        mainViewModel.insert(MessageData(messageType.toString(),
////                            newMessageTitle.toString(),
////                            newMessageContent.toString(),
////                            newMessageDate.toString()))
//                }
//            }
//        }
//


    private var time: Long = 0
    override fun onBackPressed() {
        if (System.currentTimeMillis() - time >= 2000) {
            time = System.currentTimeMillis()
            Toast.makeText(applicationContext, "뒤로 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT)
                .show()
        } else if (System.currentTimeMillis() - time < 2000) {
            this@ActivityMain.finish()
        }
    }


}


