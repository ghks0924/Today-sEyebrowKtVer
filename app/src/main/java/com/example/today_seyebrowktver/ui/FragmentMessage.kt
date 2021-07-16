package com.example.today_seyebrowktver.ui

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.today_seyebrowktver.*
import com.example.today_seyebrowktver.data.EachMessageData
import com.example.today_seyebrowktver.data.MessageData
import com.example.today_seyebrowktver.databinding.FragmentMessageBinding
import com.example.today_seyebrowktver.viewmodel.FragmentMessageViewModel
import com.example.today_seyebrowktver.viewmodel.FragmentSalesViewModel
import com.example.today_seyebrowktver.viewmodel.MainActivityViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

private val TAG = "FragmentMessages"
class FragmentMessage : Fragment() {

    val REQUEST_CREATE_MESSAGE = 1111
    val REQUEST_UPDATE_MEMO = 3333
    val REQUEST_SEND_MESSAGE = 2222


    private lateinit var binding: FragmentMessageBinding
//    private val binding get() = _binding!!

    //viewModel
    private val fragmentMessageViewModel : FragmentMessageViewModel by lazy {
        ViewModelProvider(this).get(FragmentMessageViewModel::class.java)
    }

    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    val mAuth = FirebaseAuth.getInstance()
    private var uid = mAuth.currentUser!!.uid


    //messageGroup 서버에서 받아오는 변수들
    private var adapter2: RvMessageGroupAdapter? = null
    private var messageGroupList = ArrayList<MessageGroupData>()
    private var mapByGroup: LinkedHashMap<String, MutableList<EachMessageData>>?= null

    //RecyclerView를 위한 변수들
    private var messageDataList = ArrayList<EachMessageData>()
    private var messageDisplayData = ArrayList<EachMessageData>()
    private var adapter: RvMessageAdapter? = null

    private lateinit var messagesByGroupName: LinkedHashMap<String, MutableList<MessageData>>

    private lateinit var mainViewModel: MainActivityViewModel
    private lateinit var selectedMessageType: String
    private lateinit var tempType: String
    private lateinit var tempTitle: String
    private lateinit var tempContent: String
    private lateinit var tempDate: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMessageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mainViewModel = ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)
        super.onViewCreated(view, savedInstanceState)

        setGroupData()
        setMessagData()
        setLayout()
    }

    private fun setGroupData() {
        //그룹 받아오기
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
                    messageGroupList.sortBy {it.order}

                    //메세지 데이터 받아오기
                    setMessagData()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("messageGroup", error.message)
                }


            })


    }

    private fun setMessagData() {
        database.child("users").child(uid).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val newData: ArrayList<EachMessageData> = ArrayList()
                    for (ds in snapshot.children) {
                        val EachMessageData: EachMessageData? = ds.getValue(EachMessageData::class.java)
                        if (EachMessageData != null) {
                            newData.add(EachMessageData)
                        }
                    }
                    messageDataList.clear()
                    messageDataList.addAll(newData)

                    binding.messageGroupTv.text =
                        messageGroupList[0].groupName

                    //최초 초가화일때는 자동으로 제일 첫 번째 그릅 선택
                    selectedMessageType = messageGroupList[0].groupName

                    setRv()

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("messageGroup", error.message)
                }


            })


    }


    private fun setRv() {
        messageDisplayData.clear()
        for (i in 0 until messageDataList.size){
            if (messageDataList[i].messageType == selectedMessageType){
                messageDisplayData.add(messageDataList[i])
            }
        }

        adapter = RvMessageAdapter(messageDisplayData) //adapter 생성

        //itemClick event
        adapter!!.itemClick = object : RvMessageAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(context, ActivityUpdateMessage::class.java)
                intent.putExtra("title", messageDisplayData[position].messageTitle)
                intent.putExtra("content", messageDisplayData[position].messageContent)
                intent.putExtra("date", messageDisplayData[position].messageDate)
                intent.putExtra("type", messageDisplayData[position].messageType)
                intent.putExtra("key", messageDisplayData[position].keyValue)

                startActivityForResult(intent, REQUEST_UPDATE_MEMO)
                Log.d("messageClick", "dd")

            }
        }


        //itemLongClick event
        adapter!!.itemLongClick = object : RvMessageAdapter.ItemLongClick {
            override fun onLongClick(view: View, position: Int) {

                val ab: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
                ab.setMessage("해당 문자를 삭제 하시겠습니까?")
                ab.setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                    database.child("users").child(uid).child("messages")
                        .child(messageDisplayData[position].keyValue).removeValue()
                        .addOnSuccessListener {
                            Log.d("removeValue", "삭제 성공")
                        }.addOnCanceledListener {
                            Log.d("removeValue", "삭제 실패")
                        }

                })
                ab.setNegativeButton("아니오", DialogInterface.OnClickListener { dialog, which -> })
                ab.setCancelable(true)
                ab.show()
            }
        }

        binding.messagesRv.layoutManager = GridLayoutManager(context,2)
        binding.messagesRv.adapter = adapter
    }

    private fun setLayout() {
        //fab click event
        binding.fab.setOnClickListener(View.OnClickListener {
//            val intent = Intent(context, ActivityCreateMessage::class.java)
//            intent.putExtra("type", "new")
//
//            startActivityForResult(intent, REQUEST_CREATE_MESSAGE)


            val popupMenu = PopupMenu(context, binding.fab, Gravity.BOTTOM)
            val menu = popupMenu.menu
            for (i in 0 until messageGroupList.size) {
                menu.add(Menu.NONE, i, Menu.NONE, messageGroupList[i].groupName)
            }
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
                selectedMessageType = it.toString().trim()

                setRv()
                binding.messageGroupTv.text =
                    messageGroupList[it.itemId].groupName
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

            }

            REQUEST_UPDATE_MEMO -> {

            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
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

            } else {
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


    companion object {
        fun newInstance(): FragmentMessage{
            return FragmentMessage()
        }
    }
}