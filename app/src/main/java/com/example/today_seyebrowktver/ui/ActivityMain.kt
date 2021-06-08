package com.example.today_seyebrowktver.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.today_seyebrowktver.*
import com.example.today_seyebrowktver.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ActivityMain : ActivityBase() {

    private var currentNavController: LiveData<NavController>? = null
    val navGraphIds = listOf(R.layout.example_5_fragment,
        R.layout.fragment_accounting,
        R.layout.fragment_customers,
        R.layout.fragment_memo,
        R.layout.fragment_message)

    private val REQUEST_CREATE_MEMO = 1111
    private val REQUEST_UPDATE_MEMO = 2222
    private val REQUEST_SEND_MESSAGE = 3333

    //viewBinding
    internal lateinit var binding: ActivityMainBinding

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
    var fragmentCalendar = Example5Fragment()

    //onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setPermittionCheck() //권한체크

//        //test login
//        mAuth.signInWithEmailAndPassword("ngh_0925@naver.com", "dnswjs12!@")
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d("LoginTest", "signInWithEmail:success")
//
//                    val user = mAuth.currentUser
//                    user.uid
//                    Log.d("uid", user.uid+"?")
//
//                } else {
//                    try {
//                        throw task.exception!!
//                    } catch (e: FirebaseAuthInvalidUserException) {
//                        mShowShortToast("존재하지 않는 계정 입니다")
//                    } catch (e: FirebaseAuthInvalidCredentialsException) {
//                        mShowShortToast("이메일 또는 비밀번호를 확인해주세요")
//                    } catch (e: FirebaseNetworkException) {
//                        mShowShortToast("네트워크 오류")
//                    } catch (e: Exception) {
//                        mShowShortToast("오류 : 잠시 후 다시 시도해주세요")
//                    }
//                }
//            }

        //뷰모델 생성
        mainViewModel = ViewModelProvider(this)[ViewModelMain::class.java]
        setContentView(view)

        setStatusBarColor(this, R.color.white)
        setFragments()//초기 프래그먼트 생성

//        initNavigation()



    }


    private fun initNavigation(){
//        val controller = binding.bottomNavigation.setupWithNavController(
//            navGraphIds = navGraphIds,
//            fragmentManager = supportFragmentManager,
//            containerId = R.id.main_nav_host,
//            intent = intent)
//
//                    currentNavController = controller

//        currentNavController = findNavController(R.id.main_nav_host)
//        binding.bottomNavigation.setupWithNavController(navControler)

    }

    //fragment 세팅
    fun setFragments() {
        //viewpager 없는버전
        supportFragmentManager.beginTransaction()
            .add(R.id.frame_container, fragmentCustomers, "customers")
            .addToBackStack("customers").hide(fragmentCustomers).commit()

        supportFragmentManager.beginTransaction().add(R.id.frame_container, fragmentMemo, "memo")
            .addToBackStack("memo").hide(fragmentMemo).commit()

        supportFragmentManager.beginTransaction().add(R.id.frame_container, fragmentCalendar, "home")
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
                        if (fragmentCalendar != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentCalendar).commit()
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
                        if (fragmentCalendar != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentCalendar).commit()
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
                        if (fragmentCalendar == null) {
                            fragmentCalendar = Example5Fragment()
                            supportFragmentManager.beginTransaction()
                                .add(R.id.frame_container, fragmentCalendar).commit()
                        }
                        if (fragmentCalendar != null) {
                            supportFragmentManager.beginTransaction().show(fragmentCalendar).commit()
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
                        if (fragmentCalendar != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentCalendar).commit()
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
                        if (fragmentCalendar != null) {
                            supportFragmentManager.beginTransaction().hide(fragmentCalendar).commit()
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
        intent.putExtra("type", mainViewModel.messageType.value)
        intent.putExtra("title", mainViewModel.messageTitle.value)
        intent.putExtra("content", mainViewModel.messageContent.value)
        intent.putExtra("date", mainViewModel.messageDate.value)
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

                Log.d("memoCheck", memoDate)

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

                Log.d("memoCheck", "fragment : " + newMemoDate)

                lifecycleScope.launch(Dispatchers.IO) {
                    mainViewModel.delete(mainViewModel.findMemoByDate(oldMemoDate))
                    mainViewModel.insert(MemoData(newMemoDate, memoTitle, memoContent))
                }
            }

            REQUEST_SEND_MESSAGE -> {
                val messageType = data!!.getStringExtra("type")
                val oldMessageDate = data!!.getStringExtra("oldDate")
                val newMessageDate = data!!.getStringExtra("newDate")
                val newMessageTitle = data!!.getStringExtra("title")
                val newMessageContent = data!!.getStringExtra("content")
                lifecycleScope.launch(Dispatchers.IO) {
                    mainViewModel.delete(mainViewModel.findMessageByDate(oldMessageDate))
                    mainViewModel.insert(MessageData(messageType, newMessageTitle,
                        newMessageContent, newMessageDate))
                }
            }
        }
    }

    private var time: Long = 0
    override fun onBackPressed() {
        if (System.currentTimeMillis() - time >= 2000) {
            time = System.currentTimeMillis()
            Toast.makeText(applicationContext, "뒤로 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show()
        } else if (System.currentTimeMillis() - time < 2000) {
            finish()
        }
    }

//    //Room Library Methods...
//    fun addMemo(){
//        db?.getMemeDao()?.insert(MemoData2("19:00", "제목", "내용"))
//        for (i in 0 until memoList?.size!!){
//            Log.d("Room Memo Check", "title : " + memoList!![i].memoTitle)
//        }
//    }

    override fun onSupportNavigateUp() =
        currentNavController?.value?.navigateUp() ?: false

}

