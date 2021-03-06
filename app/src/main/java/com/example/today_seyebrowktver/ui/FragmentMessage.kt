package com.example.today_seyebrowktver.ui

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.today_seyebrowktver.*
import com.example.today_seyebrowktver.data.EachMessageData
import com.example.today_seyebrowktver.data.MessageData
import com.example.today_seyebrowktver.databinding.FragmentMessageBinding
import com.example.today_seyebrowktver.viewmodel.FragmentMessageViewModel
import com.example.today_seyebrowktver.viewmodel.ActivityMainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

private val TAG = "FragmentMessages"

class FragmentMessage : Fragment() {

    val REQUEST_CREATE_MESSAGE = 1111
    val REQUEST_UPDATE_MESSAGE = 3333
    val REQUEST_SEND_MESSAGE = 2222

    //viewbinding
    private lateinit var binding: FragmentMessageBinding

    //viewModel
    private val fragmentMessageViewModel: FragmentMessageViewModel by lazy {
        ViewModelProvider(this).get(FragmentMessageViewModel::class.java)
    }

    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    val mAuth = FirebaseAuth.getInstance()
    private var uid = mAuth.currentUser!!.uid


    //messageGroup 서버에서 받아오는 변수들
    private var adapter2: RvMessageGroupAdapter? = null
    private var messageGroupList = ArrayList<MessageGroupData>()
    private var isGroupListExisted = false
    private var mapByGroup: LinkedHashMap<String, MutableList<EachMessageData>>? = null

    //RecyclerView를 위한 변수들
    private var messageDataList = ArrayList<EachMessageData>()
    private var messageDisplayData = ArrayList<EachMessageData>()
    private var adapter: RvMessageAdapter? = null

    private lateinit var messagesByGroupName: LinkedHashMap<String, MutableList<MessageData>>

    private lateinit var mainViewModel: ActivityMainViewModel
    private var selectedMessageType = ""
    private var selectedMessageGroupKeyValue = ""
    private lateinit var tempType: String
    private lateinit var tempTitle: String
    private lateinit var tempContent: String
    private lateinit var tempDate: String

    //setRv를 돌려줘야 selectedType이랑 실제 보여주는 messagelist랑 차이가 없음
    val activityForCreateMessageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            selectedMessageType = intent?.getStringExtra("type").toString() //새로 생성된 메세지 그룹 이름 받아오기
            selectedMessageGroupKeyValue =
                intent?.getStringExtra("keyValue").toString() //새로 생성된 메세지 그룹 key값 받아오기

            //view에 뿌려주기
            binding.messageGroupTv.text = selectedMessageType
            setRv()
        }
    }

    //setRv를 돌려줘야 selectedType이랑 실제 보여주는 messagelist랑 차이가 없음
    val activityForEditMessageGroupResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            //순서든, 삭제든 messageGroup의 Edit event가 발생했으면 초기값으로 돌려주기
            val intent = result.data
            selectedMessageType = intent?.getStringExtra("type").toString() //새로 생성된 메세지 그룹 이름 받아오기
            selectedMessageGroupKeyValue =
                intent?.getStringExtra("keyValue").toString() //새로 생성된 메세지 그룹 key값 받아오기

            //view에 뿌려주기
            binding.messageGroupTv.text = selectedMessageType
            setRv()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMessageBinding.inflate(inflater, container, false)

        setGroupData()
        setMessageData()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mainViewModel = ViewModelProvider(requireActivity()).get(ActivityMainViewModel::class.java)
        super.onViewCreated(view, savedInstanceState)


        setLayout()
    }

    //messageGroup 데이터가 있는지 없는지 체크
    fun checkMessageGroupAtFirst() {
        database.child("users").child(uid).child("messageGroups")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        Log.d(TAG, "messageGroup data does not exist")
                        mInitMessageGroupList()
                    } else {
                        Log.d(TAG, "messageGroup data exist!")
                        isGroupListExisted = true
                        setGroupData()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(TAG, databaseError.message)
                }
            })
    }

    //앱 초기 실행시 default messageGroup 생성
    fun mInitMessageGroupList(): Boolean {
        //기본 그룹의 keyValue 각각 생성
        val keyForReserv = database.child("users").child(uid).child("messageGroup").push().key
        val keyForRetouch = database.child("users").child(uid).child("messageGroup").push().key
        val keyForAfter = database.child("users").child(uid).child("messageGroup").push().key
        val keyForEtc = database.child("users").child(uid).child("messageGroup").push().key

        //현재시각
        val now = System.currentTimeMillis()
        val date = Date(now)
        val sdfNow = SimpleDateFormat("yyyyMMddHHmmss")
        val saveDate = sdfNow.format(date)

        val reservGroup = MessageGroupData("예약안내", 0, 0, saveDate, keyForReserv.toString(), false)
        val retouchGroup =
            MessageGroupData("리터치", 0, 1, saveDate, keyForRetouch.toString(), false)
        val afterGroup = MessageGroupData("시술후", 0, 2, saveDate, keyForAfter.toString(), false)
        val etcGroup = MessageGroupData("기타", 0, 3, saveDate, keyForEtc.toString(), false)


        var groupMap = HashMap<String, MessageGroupData>()
        groupMap.put(keyForReserv.toString(), reservGroup)
        groupMap.put(keyForRetouch.toString(), retouchGroup)
        groupMap.put(keyForAfter.toString(), afterGroup)
        groupMap.put(keyForEtc.toString(), etcGroup)

        database.child("users").child(uid).child("messageGroups").setValue(groupMap)
            .addOnSuccessListener {
                Log.d(TAG, "messageGroup : init success")

                getGroupList()

            }.addOnFailureListener {
                Log.d("errorOfCustomerSave", it.message.toString())
            }

        return isGroupListExisted
    }

    private fun setGroupData() {
        Log.d(TAG, "setGroupData Called")
        //messageGroup data가 서버에 있는지 체크한 후 없으면 기본 생성 아니면 groupList 받아오기
        getGroupList()

    }

    //그룹 받아오기
    private fun getGroupList() {
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
                    Log.e(TAG, "getGroupList() called")
                    messageGroupList.clear()
                    messageGroupList.addAll(newData)
                    messageGroupList.sortBy { it.order }

                    //초기에만 값 설정해주기
                    if (selectedMessageType == "") {
                        selectedMessageType = messageGroupList[0].groupName
                        selectedMessageGroupKeyValue = messageGroupList[0].keyValue
                    }


                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("messageGroup", error.message)
                }
            })
    }

    private fun setMessageData() {
        Log.d(TAG, "setMessageData Called")

        //메세지 받아오기
        database.child("users").child(uid).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val newData: ArrayList<EachMessageData> = ArrayList()
                    for (ds in snapshot.children) {
                        val messageData: EachMessageData? =
                            ds.getValue(EachMessageData::class.java)
                        if (messageData != null) {
                            newData.add(messageData)
                        }
                    }

                    messageDataList.clear()
                    messageDataList.addAll(newData)

                    //정렬
                    messageDataList.sortBy { it.messageDate }

                    setRv()


                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("messageGroup", error.message)
                }


            })

    }


    private fun setRv() {
        Log.d(TAG, "setRv() Called")
        //그룹별로 보여주기 위해 보여줄 메세지만 담는 list가 필요
        messageDisplayData.clear()
        for (i in 0 until messageDataList.size) {
            if (messageDataList[i].messageType == selectedMessageType) { //선택된 그룹의 메세지만 담아서 보여주기
                messageDisplayData.add(messageDataList[i])
            }
        }

        adapter = RvMessageAdapter(messageDisplayData) //adapter 생성

        //itemClick event
        adapter!!.itemClick = object : RvMessageAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(context, ActivityEachMessage::class.java)
                intent.putExtra("title", messageDisplayData[position].messageTitle)
                intent.putExtra("content", messageDisplayData[position].messageContent)
                intent.putExtra("date", messageDisplayData[position].messageDate)
                intent.putExtra("type", messageDisplayData[position].messageType)
                intent.putExtra("key", messageDisplayData[position].keyValue)

                startActivityForResult(intent, REQUEST_UPDATE_MESSAGE)
                Log.d("messageClick", "dd")

            }
        }


        //itemLongClick event
        adapter!!.itemLongClick = object : RvMessageAdapter.ItemLongClick {
            override fun onLongClick(view: View, position: Int) {

                val ab: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
                ab.setMessage("해당 문자를 삭제 하시겠습니까?")
                ab.setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                    //message 삭제
                    database.child("users").child(uid).child("messages")
                        .child(messageDisplayData[position].keyValue).removeValue()
                        .addOnSuccessListener {
                            Log.d("removeValue", "삭제 성공")
                        }.addOnCanceledListener {
                            Log.d("removeValue", "삭제 실패")
                        }


                    //삭제시 그룹별 메세지 숫자 업데이트 하기
                    checkMessagesNum()
                    setRv()


                })
                ab.setNegativeButton("아니오", DialogInterface.OnClickListener { dialog, which -> })
                ab.setCancelable(true)
                ab.show()
            }
        }

        binding.messagesRv.layoutManager = GridLayoutManager(context, 2)
        binding.messagesRv.adapter = adapter

    }

    private fun setLayout() {
        //fab click event
        mSetPopMenu()

        //더보기 event
        binding.moreIcon.setOnClickListener {
            ShowAlertDialogWithListview()
        }
    }

    //fab 클릭시 뜨는 메뉴
    fun mSetPopMenu() {
        binding.fab.setOnClickListener(View.OnClickListener {
            //group 선택 popmenu setting
            val popupMenu = PopupMenu(context, binding.fab, Gravity.BOTTOM)
            val menu = popupMenu.menu
            for (i in 0 until messageGroupList.size) {
                menu.add(Menu.NONE, i, Menu.NONE, messageGroupList[i].groupName)
            }
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
                selectedMessageType = it.toString().trim()
                selectedMessageGroupKeyValue =
                    messageGroupList[it.itemId].keyValue //그룹별 메시지수 업데이트를 위해서 선택된 그룹의 keyValue값을 받아옴
                Log.d(TAG, selectedMessageGroupKeyValue)

                setRv()
                binding.messageGroupTv.text =
                    messageGroupList[it.itemId].groupName
                return@OnMenuItemClickListener true
            })

            popupMenu.show()

        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }


    //more Icon 클릭스 띄우는 대화상자, alertDialog
    fun ShowAlertDialogWithListview() {
        val numberMethod: MutableList<String> = java.util.ArrayList()

        numberMethod.add("문자 템플릿 추가")
        numberMethod.add("문자 그룹 수정")

        //Create sequence of items
        val Animals: Array<String> = numberMethod.toTypedArray()
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setItems(Animals) { dialog, item ->
            val selectedText = Animals[item].toString() //Selected item in listview
            if (selectedText.contains("그룹")) {
                Log.d("messageGroup", "수정 버튼")

                val intent = Intent(context, ActivityEditMessageGroup::class.java)
                activityForEditMessageGroupResult.launch(intent)

            } else {
                //문자 생성
                val intent = Intent(context, ActivityCreateMessage::class.java)
                activityForCreateMessageResult.launch(intent)
            }
        }
        //Create alert dialog object via builder
        val alertDialogObject = dialogBuilder.create()
        //Show the dialog
        alertDialogObject.show()
    }

    //현재 동일한 메세지 타입의 갯수를 구한다.
    private fun checkMessagesNum() {
        database.child("users").child(uid).child("messages").orderByChild("messageType")
            .equalTo(selectedMessageType)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d(TAG, "chxeckMessagesNumber" + snapshot.childrenCount.toString())
                    val currentNumOfMsgs = snapshot.childrenCount.toInt()

                    //그룹별 메세지 수 업데이트 하기
                    database.child("users").child(uid).child("messageGroups")
                        .child(selectedMessageGroupKeyValue)
                        .child("numberOfMessages")
                        .setValue(currentNumOfMsgs)
                        .addOnSuccessListener {//새로운 메시지를 생성한 다음에 해당 메서드가 돌기 때문에 current수로 해도 됨
                            Log.d("updateMessagesNumber", "success")
                        }.addOnFailureListener {
                            Log.d("updateMessagesNumber", "fail")
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }


    companion object {
        fun newInstance(): FragmentMessage {
            val args = Bundle()
            val fragment = FragmentMessage()
            fragment.arguments = args
            return fragment
        }
    }
}