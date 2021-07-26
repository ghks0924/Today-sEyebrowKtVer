package com.example.today_seyebrowktver.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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

private const val TAG = "ActivityUpdateMemo"

class ActivityUpdateMemo : ActivityBase() {

    interface

    //viewBinding
    private

    lateinit var binding: ActivityUpdateMemoBinding

    //viewModel
    private val activityUpdateMemoViewModel: ActivityUpdateMemoViewModel by lazy {
        ViewModelProvider(this).get(ActivityUpdateMemoViewModel::class.java)
    }

    //이전 액티비티에서 받아온 메모 객체의 데이터
    private lateinit var prevMemoTitle: String
    private lateinit var prevMemoContent: String
    private lateinit var prevMemoDate: String
    private lateinit var memoId: String
    private lateinit var prevMemoInstance: MemoData

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


    private fun getIntentData() {
        val intent = intent
        prevMemoTitle = intent.getStringExtra("title").toString()
        prevMemoContent = intent.getStringExtra("content").toString()
        prevMemoDate = intent.getStringExtra("date").toString()
        memoId = intent.getStringExtra("id").toString()
    }

    private fun setLayout() {
        //memo Data 초기 세팅
        getIntentData()
        binding.memoTitleEt.setText(prevMemoTitle)
        binding.contentEdittext.setText(prevMemoContent)


        //backIv 클릭이벤트
        backIvClickEvent()

        //shareIv 클릭 이벤트
        shareIvClickEvent()


        //제목이 수정됐는지 확인해서 wasRevised = true로 바꿔줌
        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                wasRevised = true
                Log.d("wasRevised", "title : " + wasRevised)
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        }

        //내용이 수정됐는지 확인해서 wasRevised = true로 바꿔줌
        val contentWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                wasRevised = true
                Log.d("wasRevised", "content : " + wasRevised)
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        }

        binding.memoTitleEt.addTextChangedListener(titleWatcher)
        binding.contentEdittext.addTextChangedListener(contentWatcher)
    }

    private fun shareIvClickEvent() {
        binding.shareIv.setOnClickListener {

            newMemoTitle = binding.memoTitleEt.text.toString().trim()
            newMemoContent = binding.contentEdittext.text.toString().trim()

            shareMemo(newMemoTitle, newMemoContent)
        }


    }

    private fun backIvClickEvent() {
        binding.backIv.setOnClickListener(View.OnClickListener {
            newMemoTitle = binding.memoTitleEt.text.toString().trim()
            newMemoContent = binding.contentEdittext.text.toString().trim()

            //가장 먼저 focus clear
            if(binding.memoTitleEt.isFocused || binding.contentEdittext.isFocused){
                //키보드 내리고 포커스 클리어
                binding.contentEdittext.clearFocus()
                binding.memoTitleEt.clearFocus()
                binding.parentLayout.requestFocus() //기본 포커스 줘서 edittext에 포커스 안주기

                wasRevised = false
                mKeyboardDown()
            } else { //포커스를 지운다음 액션
                //내용이 수정된게 없으면 그냥 종료
                if (newMemoTitle == prevMemoTitle && newMemoContent == prevMemoContent) {
                    finish()
                } else {
                    if (wasRevised) {
                        //키보드 내리고 포커스 클리어
                        binding.contentEdittext.clearFocus()
                        binding.memoTitleEt.clearFocus()
                        binding.parentLayout.requestFocus() //기본 포커스 줘서 edittext에 포커스 안주기

                        wasRevised = false
                        mKeyboardDown()
                    } else {
                        mUpdateMemo()
                    }

                    mKeyboardDown()
                }
            }

        })
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

    //메모 생성시 처리
    private fun mUpdateMemo() {
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

        lifecycleScope.launch(Dispatchers.IO) {
            prevMemoInstance = activityUpdateMemoViewModel.findMemo(prevMemoDate)
            activityUpdateMemoViewModel.deleteMemo(prevMemoInstance)
        }


        val newMemo = MemoData(formatDate, newMemoTitle, newMemoContent, memoId)
        activityUpdateMemoViewModel.addMemo(newMemo)

        finish()
        Log.d(TAG, "memoUpdated")

    }


    override fun onBackPressed() {
        newMemoTitle = binding.memoTitleEt.text.toString().trim()
        newMemoContent = binding.contentEdittext.text.toString().trim()

        if (newMemoTitle == prevMemoTitle && newMemoContent == prevMemoContent) {
            finish()
        } else {
            if (wasRevised) {
                //키보드 내리고 포커스 클리어
                binding.contentEdittext.clearFocus()
                binding.memoTitleEt.clearFocus()
                binding.parentLayout.requestFocus() //기본 포커스 줘서 edittext에 포커스 안주기

                wasRevised = false
            } else {
                mUpdateMemo()
            }

        }
    }


}