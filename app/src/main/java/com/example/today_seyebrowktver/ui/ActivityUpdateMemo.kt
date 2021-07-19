package com.example.today_seyebrowktver.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.today_seyebrowktver.data.MemoData
import com.example.today_seyebrowktver.databinding.ActivityUpdateMemoBinding
import com.example.today_seyebrowktver.viewmodel.ActivityCreateMemoViewModel
import com.example.today_seyebrowktver.viewmodel.ActivityUpdateMemoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ActivityUpdateMemo : ActivityBase() {

    interface

    //viewBinding
    private lateinit var binding: ActivityUpdateMemoBinding

    //viewModel
    private val activityUpdateMemoViewModel: ActivityUpdateMemoViewModel by lazy {
        ViewModelProvider(this).get(ActivityUpdateMemoViewModel::class.java)
    }

    //이전 액티비티에서 받아온 메모 객체의 데이터
    private lateinit var prevMemoTitle: String
    private lateinit var prevMemoContent: String
    private lateinit var prevMemoDate: String
    private lateinit var memoId: String
    private lateinit var prevMemoInstance:MemoData

    //update할 메모의 데이터
    private lateinit var newMemoTitle: String
    private lateinit var newMemoContent: String
    private var wasRevised = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setLayout()
    }

    private fun getIntentData(){
        val intent = intent
        prevMemoTitle = intent.getStringExtra("title").toString()
        prevMemoContent = intent.getStringExtra("content").toString()
        prevMemoDate = intent.getStringExtra("date").toString()
        memoId = intent.getStringExtra("id").toString()
    }

    private fun setLayout() {
        getIntentData()
        binding.memoTitleEt.setText(prevMemoTitle)
        binding.contentEdittext.setText(prevMemoContent)

        //backIv 클릭이벤트
        binding.backIv.setOnClickListener(View.OnClickListener {
            newMemoTitle = binding.memoTitleEt.text.toString().trim()
            newMemoContent = binding.contentEdittext.text.toString().trim()

            if (newMemoTitle == prevMemoTitle && newMemoContent == prevMemoContent) {
                finish()
            } else {
                mUpdateMemo()
                mKeyboardDown()
            }
        })

        binding.memoTitleEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                wasRevised = true
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.contentEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                wasRevised = true
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    //메모 생성시 처리
    private fun mUpdateMemo() {

        //무언가를 입력했음
        if (wasRevised) {
            //키보드 내리고 포커스 클리어
            binding.contentEdittext.clearFocus()
            binding.memoTitleEt.clearFocus()
            binding.parentLayout.requestFocus() //기본 포커스 줘서 edittext에 포커스 안주기

            wasRevised = false

        } else {

            //메인 액티비티로 저장할 메모 데이터와 RESULT_OK 보내기
            // 현재시간을 msec 으로 구한다.
            val now = System.currentTimeMillis()
            // 현재시간을 date 변수에 저장한다.
            val date = Date(now)
            // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
            val sdfNow = SimpleDateFormat("yyyyMMddHHmmss")
            // nowDate 변수에 값을 저장한다.
            val formatDate = sdfNow.format(date)

            //말이 없데이트지만 지우고 uuid만 같게해서 삭제했다가 지움

            //이전 것 찾아서 지움

            lifecycleScope.launch(Dispatchers.IO){
                prevMemoInstance = activityUpdateMemoViewModel.findMemo(prevMemoDate)
                activityUpdateMemoViewModel.deleteMemo(prevMemoInstance)
            }


            val newMemo = MemoData(formatDate, newMemoTitle, newMemoContent, memoId)
            activityUpdateMemoViewModel.addMemo(newMemo)

            finish()

//            val intent = Intent()
//            intent.putExtra("title", newMemoTitle)
//            intent.putExtra("content", newMemoContent)
//            intent.putExtra("newDate", formatDate)
//            intent.putExtra("oldDate", prevMemoDate)
//            setResult(RESULT_OK, intent)
//            finish()

        }

    }


    override fun onBackPressed() {
        newMemoTitle = binding.memoTitleEt.text.toString().trim()
        newMemoContent = binding.contentEdittext.text.toString().trim()

        if (newMemoTitle == prevMemoTitle && newMemoContent == prevMemoContent) {
            finish()
        } else {
            mUpdateMemo()
            mKeyboardDown()
        }
    }


}