package com.example.today_seyebrowktver

import android.R
import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.today_seyebrowktver.databinding.FragmentMessageBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentMessage : Fragment() {

    val REQUEST_CREATE_MESSAGE = 1111
    val REQUEST_SEND_MESSAGE = 2222

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!

    //RecyclerView를 위한 변수들
    private var messageDataList = ArrayList<MessageData>()
    private var adapter: RvMessageAdapter? = null

    private lateinit var mainViewModel: ViewModelMain
    private lateinit var tempType: String
    private lateinit var tempTitle: String
    private lateinit var tempContent: String
    private lateinit var tempDate: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMessageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mainViewModel = ViewModelProvider(requireActivity()).get(ViewModelMain::class.java)
        super.onViewCreated(view, savedInstanceState)

        setData()
        setLayout()
    }

    private fun setData() {
        mainViewModel.getAllMessages().observe(activity as ActivityMain) { messages ->
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

            setRv()
        }


    }

    private fun setRv() {
        adapter = RvMessageAdapter(messageDataList) //adapter 생성

        //itemClick event
        adapter!!.itemClick = object : RvMessageAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                mainViewModel.sendMessageData(
                    messageDataList[position].messageType,
                    messageDataList[position].messageTitle,
                    messageDataList[position].messageContent,
                    messageDataList[position].messageDate
                )
                (activity as ActivityMain).mGoToSendMessageActivity()
            }
        }


        //itemLongClick event
        adapter!!.itemLongClick = object : RvMessageAdapter.ItemLongClick {
            override fun onLongClick(view: View, position: Int) {

                val ab: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
                ab.setMessage("문자를 삭제 하시겠습니까??")
                ab.setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->

                    lifecycleScope.launch(Dispatchers.IO) {
                        val messageData = mainViewModel.findMessageByDate(messageDataList[position].messageDate)
                        mainViewModel.delete(messageData)
                    }


                })
                ab.setNegativeButton("아니오", DialogInterface.OnClickListener { dialog, which -> })
                ab.setCancelable(true)
                ab.show()
            }
        }

        binding.recyclerviewMessages.layoutManager = LinearLayoutManager(context)
        binding.recyclerviewMessages.adapter = adapter
    }

    private fun setLayout() {
        //fab click event
        binding.fab.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, ActivityCreateMessage::class.java)
            intent.putExtra("type", "new")

            startActivityForResult(intent, REQUEST_CREATE_MESSAGE)
        })

        //send click event
        binding.filterCardview.setOnClickListener(View.OnClickListener {
//            val intent = Intent("android.intent.action.MAIN")
//            intent.addCategory("android.intent.category.DEFAULT")
//            intent.type = "vnd.android-dir/mms-sms"
//            startActivity(intent)
        })

        binding.fixedLayout.setOnClickListener {
            if (binding.hidenView.visibility == View.VISIBLE){
                TransitionManager.beginDelayedTransition(binding.cardview,
                    AutoTransition())
                binding.hidenView.visibility = View.GONE
                binding.historyExpandIv.setImageResource(R.drawable.arrow_down_float)
            } else{
                TransitionManager.beginDelayedTransition(binding.cardview,
                    AutoTransition())
                binding.hidenView.visibility = View.VISIBLE
                binding.historyExpandIv.setImageResource(R.drawable.arrow_up_float)
            }
        }

        binding.fixedLayout1.setOnClickListener {
            if (binding.hidenView1.visibility == View.VISIBLE){
                TransitionManager.beginDelayedTransition(binding.cardview,
                    AutoTransition())
                binding.hidenView1.visibility = View.GONE
                binding.historyExpandIv1.setImageResource(R.drawable.arrow_down_float)
            } else{
                TransitionManager.beginDelayedTransition(binding.cardview,
                    AutoTransition())
                binding.hidenView1.visibility = View.VISIBLE
                binding.historyExpandIv1.setImageResource(R.drawable.arrow_up_float)
            }
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


}