package com.example.today_seyebrowktver.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.today_seyebrowktver.DialogCreateMessageGroup
import com.example.today_seyebrowktver.MessageGroupData
import com.example.today_seyebrowktver.R
import com.example.today_seyebrowktver.RvMessageGroupAdapter
import com.example.today_seyebrowktver.databinding.ActivitySelectMessageGroupBinding
import com.example.today_seyebrowktver.databinding.ActivitySelectRegionBinding
import com.google.firebase.database.*

class ActivitySelectMessageGroup : ActivityBase() {

    //viewBinding
    private lateinit var binding: ActivitySelectMessageGroupBinding

    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var uid: String


    //for recycelrView
    private var adapter: RvMessageGroupAdapter? = null
    private var messageGroupList = ArrayList<MessageGroupData>()

    private var numOfGroups : Int = 0

    //onActivityResult 대체
    val activityForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            val type = intent?.getStringExtra("type")

            if (type == "new"){

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectMessageGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //팝업 액티비티의 크기 조절
        val dm = applicationContext.resources.displayMetrics
        val width = (dm.widthPixels * 0.9).toInt() // Display 사이즈의 90%
        val height = (dm.widthPixels * 0.8).toInt()
        window.attributes.width = width
        window.attributes.height = height

        getGroupMessageData()
        setLayout()
    }

    private fun setLayout() {
        binding.plusCardview.setOnClickListener {
            if (numOfGroups >= 7){
                mShowShortToast("메세지 그룹은 최대 7개까지 생성할 수 있습니다")
            } else {
                val intent = intent
                intent.putExtra("type", "new")
                setResult(RESULT_OK,intent)
                finish()
//            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
            }

        }
    }

    private fun getGroupMessageData() {
        val user = mAuth.currentUser
        uid = user!!.uid

        database.child("users").child(uid).child("messageGroups")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val newData: java.util.ArrayList<MessageGroupData> = java.util.ArrayList()
                    for (ds in snapshot.children) {
                        numOfGroups = snapshot.childrenCount.toInt()

                        val messageGroupData: MessageGroupData? =
                            ds.getValue(MessageGroupData::class.java)
                        if (messageGroupData != null) {
                            newData.add(messageGroupData)
                        }
                    }
                    messageGroupList.clear()
                    messageGroupList.addAll(newData)
                    messageGroupList.sortBy { it.order }

                    setRv()

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("messageGroup", error.message)
                }

            })
    }

    private fun setRv() {
        adapter = RvMessageGroupAdapter(messageGroupList)
        binding.rv.layoutManager = LinearLayoutManager(applicationContext)
        binding.rv.adapter = adapter

        adapter!!.itemClick = object : RvMessageGroupAdapter.ItemClick{
            override fun onClick(view: View, position: Int) {
                val intent = Intent()
                intent.putExtra("type", messageGroupList[position].groupName)
                intent.putExtra("keyValue",  messageGroupList[position].keyValue)
                setResult(RESULT_OK, intent)
                finish()
                overridePendingTransition(R.anim.fadein, R.anim.fadeout)
            }

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        }

    }
}