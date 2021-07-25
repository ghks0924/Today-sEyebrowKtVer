package com.example.today_seyebrowktver.ui

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
        backIvClickEvent()

        //shareIv 클릭이벤트
        shareIvClickEvent()

        //첫 화면 실행시 contentEditText에 포커스 주고 키보드 업
        binding.contentEdittext.requestFocus()
        mKeyboardUp()


        //제목만이라도 수정됐는지 확인해서 wasRevised = true로 바꿔줌
        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                wasTyped = true
                Log.d("wasRevised", "title : " + wasTyped)
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        }

        //내용만이라도 수정됐는지 확인해서 wasRevised = true로 바꿔줌
        val contentWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                wasTyped = true
                Log.d("wasRevised", "content : " + wasTyped)
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        }

        binding.memoTitleEt.addTextChangedListener(titleWatcher)
        binding.contentEdittext.addTextChangedListener(contentWatcher)

    }


    override fun onBackPressed() {
        memoTitle = binding.memoTitleEt.text.toString().trim()
        memoContent = binding.contentEdittext.text.toString().trim()

        if (memoTitle.isNullOrEmpty() && memoContent.isNullOrEmpty()) {
            finish()
            Log.d("backPressed", "finish")
        } else {
            //무언가를 입력했음
            if (wasTyped) {
                //키보드 내리고 포커스 클리어
                binding.contentEdittext.clearFocus()
                binding.memoTitleEt.clearFocus()
                binding.parentLayout.requestFocus() //기본 포커스 줘서 edittext에 포커스 안주기

                wasTyped = false

            } else {
                mCreateMemo()
            }

            Log.d("backPressed", "yes")
        }
    }

    //메모 생성시 처리
    private fun mCreateMemo() {
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

        Log.d(TAG, "memoCreated")
        finish()

    }

    private fun backIvClickEvent() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            memoTitle = binding.memoTitleEt.text.toString().trim()
            memoContent = binding.contentEdittext.text.toString().trim()

            //제목과 내용이 모두 비어있으면 아무 이벤트 없이 나가는 것으로 간주
            if (memoTitle.isNullOrEmpty() && memoContent.isNullOrEmpty()) {
                mKeyboardDown()
                finish()

            } else { //제목, 내용 둘 중 하나라도 내용이 있으면 new Memo를 생성하는 것으로 간주
                if (wasTyped) {
                    //키보드 내리고 포커스 클리어
                    binding.contentEdittext.clearFocus()
                    binding.memoTitleEt.clearFocus()
                    binding.parentLayout.requestFocus() //기본 포커스 줘서 edittext에 포커스 안주기

                    wasTyped = false
                    mKeyboardDown()
                } else {
                    mCreateMemo()
                }


            }

        })
    }

    private fun shareIvClickEvent() {
        binding.shareIv.setOnClickListener {
            memoTitle = binding.memoTitleEt.text.toString().trim()
            memoContent = binding.contentEdittext.text.toString().trim()

            shareMemo(memoTitle, memoContent)
        }
    }

    private fun shareMemo(title: String, content: String) {
        //null check해주고
        if (title.isNullOrEmpty() && content.isNullOrEmpty()) {
            mShowShortToast("공유할 메모가 비어있습니다")
        } else {
            val Sharing_intent = Intent(Intent.ACTION_SEND)
            Sharing_intent.type = "text/plain"


            val Test_Message = title + "\n\n" + content

            Sharing_intent.putExtra(Intent.EXTRA_TEXT, Test_Message)

            val Sharing = Intent.createChooser(Sharing_intent, "메모 공유하기")
            startActivity(Sharing)

            mKeyboardDown()
        }

    }
}

