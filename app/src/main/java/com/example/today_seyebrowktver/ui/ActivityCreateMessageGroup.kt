package com.example.today_seyebrowktver.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.today_seyebrowktver.EachMessageData
import com.example.today_seyebrowktver.MessageGroupData
import com.example.today_seyebrowktver.R
import com.example.today_seyebrowktver.databinding.ActivityCreateMessageGroupBinding
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class ActivityCreateMessageGroup : ActivityBase() {

    //viewBinding
    private lateinit var binding: ActivityCreateMessageGroupBinding

    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var uid: String

    private lateinit var newGroupName : String
    private lateinit var numOfGroups : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMessageGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLayout()


    }

    private fun setLayout() {
        val user = mAuth.currentUser
        uid = user.uid

        checkMessageGroupsNum()



        //키보드 자동으로 올려주기 위해서 edittext에 포커스 줌
        binding.dialogEdittext.requestFocus()

        binding.dialogCancelBtn.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        }

        binding.dialogOkBtn.setOnClickListener {
            newGroupName = binding.dialogEdittext.text.toString().trim()
            if (newGroupName.isNullOrEmpty()){
                mShowShortToast("추가할 그룹명을 입력해주세요")
            } else{
                mCreateMessageGroup()
            }
        }


    }

    private fun mCreateMessageGroup() {
        // 현재시간을 msec 으로 구한다.
        val now = System.currentTimeMillis()
        // 현재시간을 date 변수에 저장한다.
        val date = Date(now)
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        val sdfNow = SimpleDateFormat("yyyyMMddHHmmss")
        // nowDate 변수에 값을 저장한다.
        val formatDate = sdfNow.format(date) //시간

        val key = database.child("users").child(uid).child("messageGroups").push().key
        val newGroup = MessageGroupData(newGroupName,"0",Integer.parseInt(numOfGroups), formatDate, key.toString(), false)

        database.child("users").child(uid).child("messageGroups").child(key!!)
            .setValue(newGroup).addOnSuccessListener {
                mShowShortToast("새로운 메세지 그룹이 생성되었습니다")

                val intent = Intent()
                intent.putExtra("newGroupName", newGroupName)
                setResult(RESULT_OK, intent)
                finish()

            }.addOnFailureListener {
                Log.d("errorOfCustomerSave", it.message)
            }
    }

    private fun checkMessageGroupsNum() {
        database.child("users").child(uid).child("messageGroups").orderByChild("order")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    numOfGroups = snapshot.childrenCount.toString()
                    Log.d("checkMessagesNumber", snapshot.childrenCount.toString())

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}