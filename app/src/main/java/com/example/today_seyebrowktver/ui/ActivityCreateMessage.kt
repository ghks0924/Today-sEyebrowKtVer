package com.example.today_seyebrowktver.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.today_seyebrowktver.*
import com.example.today_seyebrowktver.databinding.ActivityCreateMessageBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class ActivityCreateMessage : ActivityBase() {

    private lateinit var binding: ActivityCreateMessageBinding

    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var uid: String

    private var messageGroupList = ArrayList<MessageGroupData>()

    lateinit var tempType: String
    lateinit var tempTitle: String
    lateinit var tempContent: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getGroupMessageData()
        setLayout()
    }

    private fun getGroupMessageData() {
        val user = mAuth.currentUser
        uid = user.uid

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
//            val dlg = DialogCreateMessageGroup(this)
//            dlg.start(this)

            val dlg2 = DialogShowMessageGroup(this)
            dlg2.start(this, messageGroupList)


//            Toast.makeText(applicationContext, binding.typeCardview.text, Toast.LENGTH_SHORT).show()
        })

        //save button event
        binding.saveMessageButton.setOnClickListener(View.OnClickListener {
//            tempType = binding.selectTypeTv.text.toString().trim()
            tempType = "예약안내"
            tempTitle = binding.messageTitleEt.text.toString().trim() //제목
            tempContent = binding.messageContentEt.text.toString().trim()



            if (vaildCheck()) {
                // 현재시간을 msec 으로 구한다.
                val now = System.currentTimeMillis()
                // 현재시간을 date 변수에 저장한다.
                val date = Date(now)
                // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                val sdfNow = SimpleDateFormat("yyyyMMddHHmmss")
                // nowDate 변수에 값을 저장한다.
                val formatDate = sdfNow.format(date) //시간

                val key = database.child("users").child(uid).child("messages").push().key
                val newMessage = EachMessageData(
                    tempType, tempTitle, tempContent, formatDate, key.toString())

                database.child("users").child(uid).child("messages").child(key!!)
                    .setValue(newMessage).addOnSuccessListener {
                       mShowShortToast("새로운 메세지가 생성되었습니다")

                        checkMessagesNum()
                        finish()

                    }.addOnFailureListener {
                        Log.d("errorOfCustomerSave", it.message)
                    }







//                val intent = Intent()
//                intent.putExtra("type", "문자유형")
//                intent.putExtra("title", tempTitle)
//                intent.putExtra("content", tempContent)
//                intent.putExtra("date", formatDate)
//                setResult(RESULT_OK, intent)
//                finish()


            }
        })
    }

    private fun checkMessagesNum(){
        database.child("users").child(uid).child("messages").orderByChild("messageType").equalTo(tempType)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.childrenCount
                    Log.d("checkMessagesNumber", snapshot.childrenCount.toString())

                    database.child("users").child(uid).child("messageGroups").child(tempType).child("numberOfMessages")
                        .setValue(snapshot.childrenCount.toString()).addOnSuccessListener {
                            Log.d("updateMessagesNumber", "success")
                        }.addOnFailureListener {
                            Log.d("updateMessagesNumber", "fail")
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun vaildCheck(): Boolean {
//        if (tempType.isNullOrEmpty()){
//            mShowShortToast("문자유형을 선택해주세요")
//            return false
//        }

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
}