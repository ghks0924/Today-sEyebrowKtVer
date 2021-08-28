package com.example.today_seyebrowktver.ui

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.today_seyebrowktver.*
import com.example.today_seyebrowktver.databinding.ActivityEditMessageGroupBinding
import com.google.firebase.database.*
import java.util.*

private const val TAG = "EDIT_MESSAGE_GROUP"

class ActivityEditMessageGroup : ActivityBase() {

    //viewBinding
    private lateinit var binding: ActivityEditMessageGroupBinding

    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var uid: String

    //for recycelrView
    private var adapter: RvMessageGroupEditAdapter? = null
    private var messageGroupList = ArrayList<MessageGroupData>()
    private var deletedGroupList = ArrayList<String>()

    //drag & drop
    private var callback: RvItemMoveCallback? = null
    private var touchHelper: ItemTouchHelper? = null
    private var isDragUsed = false


    //저장버튼을 누르기전 삭제 되돌리기를 위한 카운트 변수
    private var numOfExistingGroups: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditMessageGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getGroupMessageData()

    }

    fun onClick(view: View) {
        when (view) {
            binding.backIv -> finish()

            binding.saveButton -> {
                mSaveMessageGroups()
            }
        }
    }


    private fun mSaveMessageGroups() {
        if (messageGroupList.size == numOfExistingGroups && !adapter!!.isDragUsed) { //수정된게 없으면
            //일단 저장만 순서바뀐건 나중에 체크

        } else { //수정된게 있으면

            for (i in 0 until messageGroupList.size) {
                //삭제 안되는애
                if (!messageGroupList[i].deleteCheck) {
                    database.child("users").child(uid).child("messageGroups")
                        .child(messageGroupList[i].keyValue)
                        .setValue(messageGroupList[i])

//                    .addOnSuccessListener {
//                        mShowShortToast(messageGroupList[i].groupName + "업데이트 성공")
//                    }.addOnFailureListener {
//                        mShowShortToast("실패")
//                    }

                } else { //삭제 되는애
                    //삭제되는 그룹의 이름들만 모음
                    deletedGroupList.clear()
                    deletedGroupList.add(messageGroupList[i].groupName)

                    mDeleteGroup(messageGroupList[i].keyValue, messageGroupList[i].groupName)


                }
            }
            Log.d(TAG, messageGroupList[0].groupName)
            val intent = intent
            intent.putExtra("type", messageGroupList[0].groupName)
            intent.putExtra("keyValue", messageGroupList[0].keyValue)
            setResult(RESULT_OK, intent)
            finish()
        }

    }

    private fun mDeleteGroup(key: String, name:String) {
        //해당 그룹 삭제
        database.child("users").child(uid).child("messageGroups").child(key).removeValue()
            .addOnSuccessListener {
                Log.d(TAG, name + "삭제 성공")
                mCheckMessages(name)
            }.addOnCanceledListener {
                Log.d("removeValue", "삭제 실패")
            }
    }

    private fun mCheckMessages(name:String) {
        //해당 그룹에 있는 메세지 삭제
        database.child("users").child(uid).child("messages").orderByChild("messageType")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Log.d(TAG,dataSnapshot.child("messageType").toString())
                    if (dataSnapshot.child("messageType").toString() == name) {
                        val key = dataSnapshot.child("keyValue").toString()
                        mDeleteMessage(key)
                    } else {
                        mShowShortToast("이미 등록된 번호입니다")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d(TAG, databaseError.message)

                }
            })
    }

    private fun mDeleteMessage(key:String){
        //해당 메세지 삭제
        database.child("users").child(uid).child("messages").child(key).removeValue()
            .addOnSuccessListener {
                Log.d(TAG,key.toString() + "삭제")
                Log.d("removeValue", "삭제 성공")
            }.addOnCanceledListener {
                Log.d("removeValue", "삭제 실패")
            }
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
                        }
                    }
                    messageGroupList.clear()
                    messageGroupList.addAll(newData)
                    messageGroupList.sortBy { it.order } //저장된 순서로 오름차순 정렬

                    //초기값 세팅 = 전체 그룹의 수
                    numOfExistingGroups = messageGroupList.size

                    setRv()

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("messageGroup", error.message)
                }

            })
    }

    private fun setRv() {
        adapter = RvMessageGroupEditAdapter(messageGroupList)
        callback = RvItemMoveCallback(adapter!!)
        touchHelper = ItemTouchHelper(callback!!)
        touchHelper!!.attachToRecyclerView(binding.rv)

        binding.rv.layoutManager = LinearLayoutManager(applicationContext)
        binding.rv.adapter = adapter

        val dividerDecoration: DividerItemDecoration =
            DividerItemDecoration(
                binding.rv.context,
                LinearLayoutManager(applicationContext).orientation
            )

        binding.rv.addItemDecoration(dividerDecoration)

        //삭제 버튼 이벤트
        adapter!!.deleteClick = object : RvMessageGroupEditAdapter.DeleteIconClick {
            override fun onClick(view: View, position: Int) {
                Log.d("restoreRvItem", "deleteClicked")
                if (!isFinishing) {
                    val ab: android.app.AlertDialog.Builder =
                        android.app.AlertDialog.Builder(this@ActivityEditMessageGroup)
                    ab.setTitle(messageGroupList[position].groupName + " 문자 그룹 삭제")
                    ab.setMessage("위 그룹에 포함된 모든 메세지가 함께 삭제됩니다")
                    ab.setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->

                        // "예" 버튼 클릭시

//                    database.child("users").child(uid).child("messages")
//                        .child(messageDisplayData[position].keyValue).removeValue()
//                        .addOnSuccessListener {
//                            Log.d("removeValue", "삭제 성공")
//                        }.addOnCanceledListener {
//                            Log.d("removeValue", "삭제 실패")
//                        }
//


                        numOfExistingGroups -= 1 //살아있는 그룹 수 -1
                        messageGroupList[position].deleteCheck = true //adapter에서 인지해야되는 boolean값
                        adapter!!.notifyDataSetChanged()

                        //똑같은 애를 일단 ArrayList 맨뒤에 추가해줌
                        messageGroupList.add(messageGroupList[position])
                        messageGroupList.removeAt(position) //그리고 이전 위치 삭제해주고

                        //순서 재정리
                        for (i in 0 until messageGroupList.size) {
                            messageGroupList[i].order = i
                        }

                        adapter!!.notifyDataSetChanged()

                    })
                    ab.setNegativeButton("아니오",
                        DialogInterface.OnClickListener { dialog, which -> })
                    ab.setCancelable(true)
                    ab.show()
                }


            }

        }

        //plus 버튼 이벤트
        adapter!!.plusClick = object : RvMessageGroupEditAdapter.PlusIconClick {
            override fun onPlusClick(view: View, position: Int) {
                if (!isFinishing) {

                    //삭제 체크 값을 false
                    messageGroupList[position].deleteCheck = false

                    //삭제 체크가 풀린애를 맨 뒤에 넣기위해서 일단, 삭제리스트에 있는 아이들의 order값에 전부 +1
                    for (i in 0 until messageGroupList.size) {
                        if (messageGroupList[i].order >= numOfExistingGroups) {
                            messageGroupList[i].order += 1
                        }
                    }

                    //삭제 체크를 푼 애의 순서를 마지막으로 넣어준다
                    messageGroupList[position].order = numOfExistingGroups
                    numOfExistingGroups += 1 // 살아있는 그룹수 +1

                    //위에서 +1해준 더미값이 있기때문에, 순서에 맞는 order값으로 재정리
                    messageGroupList.sortBy { it.order }
                    for (i in 0 until messageGroupList.size) {
                        messageGroupList[i].order = i
                    }


                    adapter!!.notifyDataSetChanged()

                }


            }

        }
        adapter!!.itemLongClick = object : RvMessageGroupEditAdapter.ItemLongClick {
            override fun onLongClick(view: View, position: Int) {
            }

        }
    }

}