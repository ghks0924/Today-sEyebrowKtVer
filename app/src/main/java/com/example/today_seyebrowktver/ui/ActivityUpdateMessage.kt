package com.example.today_seyebrowktver.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.today_seyebrowktver.BottomSheetFragmentMessageUpdateCheck
import com.example.today_seyebrowktver.data.EachMessageData
import com.example.today_seyebrowktver.databinding.ActivityUpdateMessageBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "UPDATE_MESSAGE"

class ActivityUpdateMessage : ActivityBase() {

    //viewBinding
    private lateinit var binding: ActivityUpdateMessageBinding

    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var uid: String

    //이전 액티비티에서 받아온 message 객체의 데이터
    private lateinit var prevMessageTitle: String
    private lateinit var prevMessageContent: String
    private lateinit var prevMessageDate: String
    private lateinit var prevMessageType: String
    private lateinit var prevKeyValue: String

    private lateinit var selectedGroupKey:String



    //update할 메세지의 데이터
    private lateinit var newMsgType: String
    private lateinit var newMsgTitle: String
    private lateinit var newMsgContent: String
    private var wasRevised = false

    //onActivityResult 대체
    //Message Group Select popup activity를 위한 처리
    val activityForSelectGroupResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            val type = intent?.getStringExtra("type")
            val keyValue = intent?.getStringExtra("keyValue") //key값도 받아와야 그룹별 메세지 수를 업데이트 할 수 있음
            selectedGroupKey = keyValue.toString()

            if (type == "new") { // 새로운 타입을 만들면
                mShowShortToast("새로운 그룹 만들거고")
                mCreateMessageGroup()
            }
            else {
                newMsgType = type.toString()  //이전 type과 변경된 사항이 있는지 나중에 확인하기위한 변수임
                binding.selectedTypeTv.text = type //view에 새로운 그룹이름 적용

                //그룹별 메세지 수 업데이트

            }
        }
    }

    //Message Group Create activity를 위한 처리
    val activityForNewMessageGroupResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            val newGroupName = intent?.getStringExtra("newGroupName")
            val newGroupKey = intent?.getStringExtra("newGroupKey")

            //새로 생성된 그룹의 이름이 있으면 view에 적용한다
            if (!newGroupName.isNullOrEmpty()){
                binding.selectedTypeTv.text = newGroupName
                selectedGroupKey = newGroupKey.toString() //새로운 그룹 키 가져와서 ㄱㄱ

                //그룹별 메세지 수 업데이트
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val user = mAuth.currentUser
        uid = user!!.uid

        setLayout()
    }

    //이전 액티비티에서 받아오는 메서드
    private fun getDataFromPrevActivity() {
        val intent = intent
        prevMessageTitle = intent.getStringExtra("title").toString()
        prevMessageContent = intent.getStringExtra("content").toString()
        prevMessageDate = intent.getStringExtra("date").toString()
        prevMessageType = intent.getStringExtra("type").toString()
        prevKeyValue = intent.getStringExtra("keyValue").toString()
        Log.d(TAG, "title : " + prevMessageTitle)
        Log.d(TAG, "type : " + prevMessageType)
    }

    private fun setLayout() {
        getDataFromPrevActivity()

        binding.messageTitleEt.setText(prevMessageTitle)
        binding.messageContentEt.setText(prevMessageContent)
        binding.selectedTypeTv.text = prevMessageType

        //backIv 클릭이벤트
        binding.backIv.setOnClickListener {
            //수정사항이 있는지 체크
            if (isAnythingRevised()) { //수정사항이 있으면
                val frag = BottomSheetFragmentMessageUpdateCheck()
                frag.show(supportFragmentManager, frag.tag)
            } else { //수정사항이 없으면
                finish()
            }
        }

        binding.typeCardview.setOnClickListener {
            val intent = Intent(applicationContext, ActivitySelectMessageGroup::class.java)
            activityForSelectGroupResult.launch(intent)
        }

        binding.messageTitleEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                wasRevised = true
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.messageContentEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                wasRevised = true
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.saveMessageButton.setOnClickListener {
            if (!isAnythingRevised()) { //수정된 사항이 없으면
                mShowShortToast("변경된 내용이 없습니다")
            } else { //변경된 사항이 있으면 저장하고 나가기
                val frag = BottomSheetFragmentMessageUpdateCheck()
                frag.show(supportFragmentManager, frag.tag)
            }
        }
    }

    //새로운 메세지 그룹 생성을 위한 액티비티를 띄운다.
    private fun mCreateMessageGroup() {
        val intent = Intent(applicationContext, ActivityCreateMessageGroup::class.java)
        activityForNewMessageGroupResult.launch(intent)
    }

    //업데이트 정보중 비어있는 내용은 없는지 체크
    private fun isValidCheck(): Boolean {
        //제목이 비어있으면 안됨
        if (binding.messageTitleEt.text.isNullOrEmpty()) {
            mShowShortToast("문자 제목을 입력해주세요")
            return false
        }
        //내용이 비어있으면 안됨
        if (binding.messageContentEt.text.isNullOrEmpty()) {
            mShowShortToast("문자 내용을 입력해주세요")
            return false
        }

        return true
    }

    //변경된 내용이 실제로 있늕지 체크
    private fun isAnythingRevised(): Boolean {
        if (prevMessageType != binding.selectedTypeTv.text) {
            return true
        }
        if (prevMessageTitle != binding.messageTitleEt.text.toString()) {
            return true
        }
        if (prevMessageContent != binding.messageContentEt.text.toString()) {
            return true
        }

        return false
    }

    //서버 && 이전 액티비티로 데이터 넘김
    fun mSaveUpdatedMessageData() {
        if (isValidCheck()) {
            val now = System.currentTimeMillis()
            val date = Date(now)
            val sdfNow = SimpleDateFormat("yyyyMMddHHmmss")
            val updatedDate = sdfNow.format(date)

            newMsgType = binding.selectedTypeTv.text.toString().trim()
            newMsgTitle = binding.messageTitleEt.text.toString().trim()
            newMsgContent = binding.messageContentEt.text.toString().trim()

            val updatedMessage =
                EachMessageData(newMsgType, newMsgTitle, newMsgContent, updatedDate, prevKeyValue)

            database.child("users").child(uid).child("messages").child(prevKeyValue)
                .setValue(updatedMessage)
                .addOnSuccessListener {

                }
                .addOnFailureListener {

                }

            val intent = intent

            intent.putExtra("edittedType", newMsgType)
            intent.putExtra("edittedTitle", newMsgTitle)
            intent.putExtra("edittedContent", newMsgContent)
            setResult(RESULT_OK, intent)
            finish()
        }
    }


    override fun onBackPressed() {
        //수정사항이 있는지 체크
        if (isAnythingRevised()) { //수정사항이 있으면
            val frag = BottomSheetFragmentMessageUpdateCheck()
            frag.show(supportFragmentManager, frag.tag)
        } else { //수정사항이 없으면
            finish()
        }

    }

}