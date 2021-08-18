package com.example.today_seyebrowktver.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.today_seyebrowktver.*
import com.example.today_seyebrowktver.data.EachMessageData
import com.example.today_seyebrowktver.databinding.ActivityCreateMessageBinding
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "ActivityCreateMessage"
class ActivityCreateMessage : ActivityBase() {

    val REQUEST_SELECT_MESSAGE_GROUP = 1111
    val REQUEST_CREATE_MESSAGE_GROUP = 2222

    private lateinit var binding: ActivityCreateMessageBinding

    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var uid: String


    private var messageGroupList = ArrayList<MessageGroupData>()

    private lateinit var tempType: String
    private lateinit var tempTitle: String
    private lateinit var tempContent: String
    private lateinit var selectedGroupKey:String

    private var currentNumOfMsgs: Int = 0

    private lateinit var newMsgType: String
    private lateinit var newMsgGroupKey:String

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
                binding.selectedTypeTv.text = type //view에 새로운 그룹이름 적용
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
                mShowShortToast("새로운 그룹이 생성됐고")
                binding.selectedTypeTv.text = newGroupName
                selectedGroupKey = newGroupKey.toString() //새로운 그룹 키 가져와서 ㄱㄱ
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getGroupMessageData()
        setLayout()
    }

    private fun getGroupMessageData() {
        val user = mAuth.currentUser
        uid = user!!.uid

        database.child("users").child(uid).child("messageGroups")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val newData: ArrayList<MessageGroupData> = ArrayList()
                    for (ds in snapshot.children) {
                        val messageGroupData: MessageGroupData? =
                            ds.getValue(MessageGroupData::class.java)
                        if (messageGroupData != null) {
                            newData.add(messageGroupData)
                            Log.d("messageGroup", messageGroupData.groupName)
                            Log.d("messageGroup", messageGroupData.order.toString())
                            Log.d("messageGroup", messageGroupData.numberOfMessages.toString())
                            Log.d("messageGroup", messageGroupData.savedate)
                        }
                    }
                    messageGroupList.clear()
                    messageGroupList.addAll(newData)
                    messageGroupList.sortBy { it.order }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("messageGroup", error.message)
                }

            })
    }

    private fun setLayout() {

        //back_iv 뒤로가기 event
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })

        binding.typeCardview.setOnClickListener(View.OnClickListener {
            //메세지 그룹 선택 대화상자
            val intent = Intent(applicationContext, ActivitySelectMessageGroup::class.java)
            activityForSelectGroupResult.launch(intent)
        })

        //save button event
        binding.saveMessageButton.setOnClickListener(View.OnClickListener {
            tempType = binding.selectedTypeTv.text.toString().trim()//유형
            tempTitle = binding.messageTitleEt.text.toString().trim() //제목
            tempContent = binding.messageContentEt.text.toString().trim()


            if (vaildCheck()) {
                //메세지 생성
                mCreateMessage()
            }
        })
    }

    //새로운 메세지 생성하는 메서드
    private fun mCreateMessage(){
        val now = System.currentTimeMillis()
        val date = Date(now)
        val sdfNow = SimpleDateFormat("yyyyMMddHHmmss")
        val formatDate = sdfNow.format(date) //시간

        val key = database.child("users").child(uid).child("messages").push().key
        val newMessage = EachMessageData(
            tempType, tempTitle, tempContent, formatDate, key.toString())
        //새로운 메세지를 생성
        database.child("users").child(uid).child("messages").child(key!!)
            .setValue(newMessage).addOnSuccessListener {
                mShowShortToast("새로운 메세지가 생성되었습니다")

                checkMessagesNum()
                val intent = intent
                setResult(RESULT_OK, intent)
                finish()

            }.addOnFailureListener {
                Log.d("errorOfCustomerSave", it.message.toString())
            }
    }

    //현재 동일한 메세지 타입의 갯수를 구한다.
    private fun checkMessagesNum() {
        database.child("users").child(uid).child("messages").orderByChild("messageType")
            .equalTo(tempType)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d(TAG, "checkMessagesNumber" + snapshot.childrenCount.toString())
                    currentNumOfMsgs = snapshot.childrenCount.toInt()

                    //그룹별 메세지 수 업데이트 하기
                    database.child("users").child(uid).child("messageGroups").child(selectedGroupKey)
                        .child("numberOfMessages")
                        .setValue(currentNumOfMsgs.toString()).addOnSuccessListener {//새로운 메시지를 생성한 다음에 해당 메서드가 돌기 때문에 current수로 해도 됨
                            Log.d("updateMessagesNumber", "success")
                        }.addOnFailureListener {
                            Log.d("updateMessagesNumber", "fail")
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }

    //문자 생성을 위한 유효성 체크
    private fun vaildCheck(): Boolean {
        if (tempType.isNullOrEmpty()) {
            mShowShortToast("문자유형을 선택해주세요")
            return false
        }

        if (tempTitle.isNullOrEmpty()) {
            mShowShortToast("문자 제목을 입력해주세요")
            return false
        }

        if (tempContent.isNullOrEmpty()) {
            mShowShortToast("문자 내용을 입력해주세요")
            return false
        }

        return true
    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (resultCode != RESULT_OK) {
//            return
//        }
//
//        when (requestCode) {
//            REQUEST_SELECT_MESSAGE_GROUP -> {
//                if (data!!.getStringExtra("type").toString() == "new") {
//                    val intent = Intent(applicationContext, ActivityCreateMessageGroup::class.java)
//                    startActivityForResult(intent, REQUEST_CREATE_MESSAGE_GROUP)
//                } else {
//                    binding.selectedTypeTv.text = data!!.getStringExtra("type")
//                    binding.selectedTypeTv.setTextColor(Color.parseColor("#221f20"))
//
//                }
//            }
//
//            REQUEST_CREATE_MESSAGE_GROUP ->{
//                binding.selectedTypeTv.text = data!!.getStringExtra("newGroupName")
//                binding.selectedTypeTv.setTextColor(Color.parseColor("#221f20"))
//            }
//        }
//    }

    //새로운 메세지 그룹 생성을 위한 액티비티를 띄운다.
    private fun mCreateMessageGroup() {
        val intent = Intent(applicationContext, ActivityCreateMessageGroup::class.java)
        activityForNewMessageGroupResult.launch(intent)
    }
}