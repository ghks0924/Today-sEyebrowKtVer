package com.example.today_seyebrowktver.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.today_seyebrowktver.data.MemoData
import com.example.today_seyebrowktver.databinding.ActivityCreateMemoBinding
import com.example.today_seyebrowktver.viewmodel.ActivityCreateMemoViewModel
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "ActivityCreateMemo"

class ActivityCreateMemo : ActivityBase() {

    //viewBinding
    private lateinit var binding: ActivityCreateMemoBinding

    //viewModel
    private val activityCreateMemoViewModel: ActivityCreateMemoViewModel by lazy {
        ViewModelProvider(this).get(ActivityCreateMemoViewModel::class.java)
    }

    //memo 객체를 받을 변수들
    private var wasTyped = false
    private lateinit var memoTitle: String
    private lateinit var memoContent: String
    private lateinit var memoDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLayout()
    }

    private fun setLayout() {

        //backIv 클릭이벤트
        binding.backIv.setOnClickListener(View.OnClickListener {
            memoTitle = binding.memoTitleEt.text.toString().trim()
            memoContent = binding.contentEdittext.text.toString().trim()

            //제목과 내용이 모두 비어있으면 아무 이벤트 없이 나가는 것으로 간주
            if (memoTitle.isNullOrEmpty() && memoContent.isNullOrEmpty()) {
                mKeyboardDown()
                finish()

            } else { //제목, 내용 둘 중 하나라도 내용이 있으면 new Memo를 생성하는 것으로 간주
                mCreateMemo()
                mKeyboardDown()
            }

        })

        //contentEditText에 포커스 주고 키보드 업
        binding.contentEdittext.requestFocus()
        mKeyboardUp()


        //내용만이라도 수정됐는지 확인해서 wasRevised = true로 바꿔줌
        binding.contentEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                wasTyped = true
                Log.d("wasRevised", "content : " + wasTyped)
            }

            override fun afterTextChanged(s: Editable) {}
        })

        //제목만이라도 수정됐는지 확인해서 wasRevised = true로 바꿔줌
        binding.memoTitleEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                wasTyped = true
                Log.d("wasRevised", "title : " + wasTyped)
            }

            override fun afterTextChanged(s: Editable?) {}

        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        memoTitle = binding.memoTitleEt.text.toString().trim()
        memoContent = binding.contentEdittext.text.toString().trim()

        if (memoTitle.isNullOrEmpty() && memoContent.isNullOrEmpty()) {
            finish()
        } else {
            mCreateMemo()
        }
    }


//    //메모 업데이트 처리
//    private fun mUpdateMemo() {
//        //아무것도 수정도 안했거나 수정했어도 이전과 내용이 달라진게 없으면
//        if ((nowMemoTitle == memoTitleFromPrevAct && nowMemoContent == memoContentFromPrevAct)) {
//            wasRevised = false
//            mKeyboardDown()
//            finish()
//        } else { //무언가가 수정이 됐어
//            if (wasRevised && (nowMemoTitle != memoTitleFromPrevAct || nowMemoContent != memoContentFromPrevAct)) {
//                //키보드 내리고 포커스 클리어
//                binding.contentEdittext.clearFocus()
//                binding.memoTitleEt.clearFocus()
//                binding.parentLayout.requestFocus() //기본 포커스 줘서 edittext에 포커스 안주기
//
//                wasRevised = false
//
//            } else {
//
//                //메인 액티비티로 저장할 메모 데이터와 RESULT_OK 보내기
//
//                // 현재시간을 msec 으로 구한다.
//                val now = System.currentTimeMillis()
//                // 현재시간을 date 변수에 저장한다.
//                val date = Date(now)
//                // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
//                val sdfNow = SimpleDateFormat("yyyyMMddHHmmss")
//                // nowDate 변수에 값을 저장한다.
//                val formatDate = sdfNow.format(date)
//
//                val intent = Intent()
//                intent.putExtra("memoTitle", nowMemoTitle)
//                intent.putExtra("memoContent", nowMemoContent)
//                intent.putExtra("prevDate", memoDateFromPrevAct)
//                intent.putExtra("memoDate", formatDate)
//                intent.putExtra("type", type)
//                setResult(RESULT_OK, intent)
//                finish()
//
//            }
//        }
//    }

    //메모 생성시 처리
    private fun mCreateMemo() {
        //무언가를 입력했음
        if (wasTyped) {
            //키보드 내리고 포커스 클리어
            binding.contentEdittext.clearFocus()
            binding.memoTitleEt.clearFocus()
            binding.parentLayout.requestFocus() //기본 포커스 줘서 edittext에 포커스 안주기

            wasTyped = false

        } else {

            Log.d("getMemo", "update? :")
            //메인 액티비티로 저장할 메모 데이터와 RESULT_OK 보내기

            // 현재시간을 msec 으로 구한다.
            val now = System.currentTimeMillis()
            // 현재시간을 date 변수에 저장한다.
            val date = Date(now)
            // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
            val sdfNow = SimpleDateFormat("yyyyMMddHHmmss")
            // nowDate 변수에 값을 저장한다.
            val formatDate = sdfNow.format(date)

            activityCreateMemoViewModel.addMemo(
                MemoData(formatDate, memoTitle, memoContent, UUID.randomUUID().toString())
            )

//            val intent = Intent()
//            intent.putExtra("title", memoTitle)
//            intent.putExtra("content", memoContent)
//            intent.putExtra("date", formatDate)
//            intent.putExtra("id", UUID.randomUUID().toString()+"/"+formatDate)
//            setResult(Activity.RESULT_OK, intent)

            if(isFinishing) finish()

        }

    }
}

