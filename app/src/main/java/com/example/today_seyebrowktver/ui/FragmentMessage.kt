package com.example.today_seyebrowktver.ui

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.today_seyebrowktver.*
import com.example.today_seyebrowktver.databinding.FragmentMessageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentMessage : Fragment() {

    val REQUEST_CREATE_MESSAGE = 1111
    val REQUEST_SEND_MESSAGE = 2222

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    val mAuth = FirebaseAuth.getInstance()
    private lateinit var uid: String




    //messageGroup 서버에서 받아오는 변수들
    private var adapter2: RvMessageGroupAdapter? = null
    private var messageGroupList = ArrayList<MessageGroupData>()

    //RecyclerView를 위한 변수들
    private var messageDataList = ArrayList<MessageData>()
    private var adapter: RvMessageAdapter? = null

    private lateinit var messagesByGroupName : LinkedHashMap<String, MutableList<MessageData>>

    private lateinit var mainViewModel: ViewModelMain
    private lateinit var tempType: String
    private lateinit var tempTitle: String
    private lateinit var tempContent: String
    private lateinit var tempDate: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMessageBinding.inflate(inflater, container, false)

        val user = mAuth.currentUser
        uid = user.uid
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mainViewModel = ViewModelProvider(requireActivity()).get(ViewModelMain::class.java)
        super.onViewCreated(view, savedInstanceState)

        setData()
        setLayout()
    }

    private fun setData() {
        //그룹 받아오기
        database.child("users").child(uid).child("messageGroups")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val newData: ArrayList<MessageGroupData> = ArrayList()
                    for (ds in snapshot.children) {
                        val messageGroupData: MessageGroupData? = ds.getValue(MessageGroupData::class.java)
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

                    binding.messageGroupTv.text = messageGroupList[0].groupName + "("+messageGroupList[0].numberOfMessages+")"

                    //메세지 데이터 받아오기
                    setMessagData()


                    setGroupRv()

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("messageGroup", error.message)
                }


            })


    }

    private fun setMessagData() {
        database.child("users").child(uid).child("messageGroups")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val newData: ArrayList<MessageGroupData> = ArrayList()
                    for (ds in snapshot.children) {
                        val messageGroupData: MessageGroupData? = ds.getValue(MessageGroupData::class.java)
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

                    binding.messageGroupTv.text = messageGroupList[0].groupName + "("+messageGroupList[0].numberOfMessages+")"

                    //메세지 데이터 받아오기
                    setMessagData()


                    setGroupRv()

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("messageGroup", error.message)
                }


            })


    }

    private fun setGroupRv(){
        adapter2 = RvMessageGroupAdapter(messageGroupList)

        binding.groupRv.layoutManager = LinearLayoutManager(context)
        binding.groupRv.adapter = adapter2

        setOnItemClickListener()
    }

    private fun setOnItemClickListener() {
        adapter2!!.itemClick = object : RvMessageGroupAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {

            Log.d("messageGroupRv", messageGroupList[position].groupName)
            }


            }


        }

//    private fun setRv() {
//        adapter = RvMessageAdapter(messageDataList) //adapter 생성
//
//        //itemClick event
//        adapter!!.itemClick = object : RvMessageAdapter.ItemClick {
//            override fun onClick(view: View, position: Int) {
//                mainViewModel.sendMessageData(
//                    messageDataList[position].messageType,
//                    messageDataList[position].messageTitle,
//                    messageDataList[position].messageContent,
//                    messageDataList[position].messageDate
//                )
//                (activity as ActivityMain).mGoToSendMessageActivity()
//            }
//        }
//
//
//        //itemLongClick event
//        adapter!!.itemLongClick = object : RvMessageAdapter.ItemLongClick {
//            override fun onLongClick(view: View, position: Int) {
//
//                val ab: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
//                ab.setMessage("문자를 삭제 하시겠습니까??")
//                ab.setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
//
//                    lifecycleScope.launch(Dispatchers.IO) {
//                        val messageData =
//                            mainViewModel.findMessageByDate(messageDataList[position].messageDate)
//                        mainViewModel.delete(messageData)
//                    }
//
//
//                })
//                ab.setNegativeButton("아니오", DialogInterface.OnClickListener { dialog, which -> })
//                ab.setCancelable(true)
//                ab.show()
//            }
//        }
//
//        binding.reservMessages.layoutManager = LinearLayoutManager(context)
//        binding.reservMessages.adapter = adapter
//    }
//
    private fun setLayout() {
        //fab click event
        binding.fab.setOnClickListener(View.OnClickListener {
//            val intent = Intent(context, ActivityCreateMessage::class.java)
//            intent.putExtra("type", "new")
//
//            startActivityForResult(intent, REQUEST_CREATE_MESSAGE)


            val popupMenu = PopupMenu(context, binding.fab, Gravity.BOTTOM)
            val menu = popupMenu.menu
            for (i in 0 until  messageGroupList.size){
                menu.add(Menu.NONE, i , Menu.NONE, messageGroupList[i].groupName)
            }
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
                Log.d("menuItem", it.itemId.toString())
                binding.messageGroupTv.text = messageGroupList[it.itemId].groupName
                return@OnMenuItemClickListener true
            })

            popupMenu.show()

        })



//        send click event
//        binding.filterCardview.setOnClickListener(View.OnClickListener {
//            val intent = Intent("android.intent.action.MAIN")
//            intent.addCategory("android.intent.category.DEFAULT")
//            intent.type = "vnd.android-dir/mms-sms"
//            startActivity(intent)
//        })
//
//        binding.eventFixedLayout.setOnClickListener {
//            if (binding.eventHidenView.visibility == View.VISIBLE) {
////                TransitionManager.beginDelayedTransition(binding.cardview,
////                    AutoTransition())
//                binding.eventHidenView.visibility = View.GONE
//                binding.eventHistoryExpandIv.setImageResource(com.example.today_seyebrowktver.R.drawable.outline_expand_more_black_36)
//            } else {
////                TransitionManager.beginDelayedTransition(binding.cardview,
////                    AutoTransition())
//                binding.eventHidenView.visibility = View.VISIBLE
//                binding.eventHistoryExpandIv.setImageResource(com.example.today_seyebrowktver.R.drawable.outline_expand_less_black_36)
//            }
//        }
//
//        binding.afterFixedLayout.setOnClickListener {
//            if (binding.afterHidenView.visibility == View.VISIBLE) {
////                TransitionManager.beginDelayedTransition(binding.cardview,
////                    AutoTransition())
//                binding.afterHidenView.visibility = View.GONE
//                binding.afterHistoryExpandIv.setImageResource(com.example.today_seyebrowktver.R.drawable.outline_expand_more_black_36)
//            } else {
////                TransitionManager.beginDelayedTransition(binding.cardview,
////                    AutoTransition())
//                binding.afterHidenView.visibility = View.VISIBLE
//                binding.afterHistoryExpandIv.setImageResource(com.example.today_seyebrowktver.R.drawable.outline_expand_less_black_36)
//            }
//        }
//
//        binding.retouchFixedLayout.setOnClickListener {
//            if (binding.retouchHidenView.visibility == View.VISIBLE) {
////                TransitionManager.beginDelayedTransition(binding.cardview,
////                    AutoTransition())
//                binding.retouchHidenView.visibility = View.GONE
//                binding.retouchHistoryExpandIv.setImageResource(com.example.today_seyebrowktver.R.drawable.outline_expand_more_black_36)
//            } else {
////                TransitionManager.beginDelayedTransition(binding.cardview,
////                    AutoTransition())
//                binding.retouchHidenView.visibility = View.VISIBLE
//                binding.retouchHistoryExpandIv.setImageResource(com.example.today_seyebrowktver.R.drawable.outline_expand_less_black_36)
//            }
//        }
//
//        binding.extraFixedLayout.setOnClickListener {
//            if (binding.extraHidenView.visibility == View.VISIBLE) {
////                TransitionManager.beginDelayedTransition(binding.cardview,
////                    AutoTransition())
//                binding.extraHidenView.visibility = View.GONE
//                binding.extraHistoryExpandIv.setImageResource(com.example.today_seyebrowktver.R.drawable.outline_expand_more_black_36)
//            } else {
////                TransitionManager.beginDelayedTransition(binding.cardview,
////                    AutoTransition())
//                binding.extraHidenView.visibility = View.VISIBLE
//                binding.extraHistoryExpandIv.setImageResource(com.example.today_seyebrowktver.R.drawable.outline_expand_less_black_36)
//            }
//        }
//
        binding.moreIcon.setOnClickListener {
            ShowAlertDialogWithListview()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK) {
            return
        }

        when (requestCode) {
            REQUEST_CREATE_MESSAGE -> {
                //ActivityCreateMessage에서 넘어온 데이터 받기
                val messageType = data!!.getStringExtra("type")
                val messageTilte = data!!.getStringExtra("title")
                val messageContent = data!!.getStringExtra("content")
                val messageDate = data!!.getStringExtra("date")

                lifecycleScope.launch(Dispatchers.IO) {
                    mainViewModel.insert(
                        MessageData(
                            messageType,
                            messageTilte,
                            messageContent,
                            messageDate
                        )
                    )
                }

                mainViewModel.getAllMessages()
                    .observe(activity as ActivityMain, Observer { messages ->
                        var tempMessageDataList = ArrayList<MessageData>()
                        for (i in 0 until messages.size) {
                            tempMessageDataList.add(
                                MessageData(
                                    messages[i].messageType,
                                    messages[i].messageTitle,
                                    messages[i].messageContent,
                                    messages[i].messageDate
                                )
                            )
                        }
                        messageDataList.clear()
                        messageDataList.addAll(tempMessageDataList)
                    })
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                Log.d("messageGroup", "추가 버튼")

                val intent = Intent(context, ActivityEditMessageGroup::class.java)
                startActivity(intent)

            }  else {
                val intent = Intent(context, ActivityCreateMessage::class.java)
                startActivity(intent)
                Log.d("messageGroup", "순서 수정 버튼")
            }
        }
        //Create alert dialog object via builder
        val alertDialogObject = dialogBuilder.create()
        //Show the dialog
        alertDialogObject.show()
    }


}