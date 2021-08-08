package com.example.today_seyebrowktver.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.today_seyebrowktver.MessageGroupData
import com.example.today_seyebrowktver.TotalRepository
import com.example.today_seyebrowktver.data.EachMessageData

private const val TAG = "FragmentMessageViewModel"
class FragmentMessageViewModel(application: Application) : AndroidViewModel(application) {

    private val totalRepository = TotalRepository.get()

    var messageGroupList = totalRepository.getMessageGroupList()
    var messagesList = totalRepository.getMessagesList()


    //앱 최초 실행인지 아닌지 확인해서 생성 or 아무일도 안 일어나기
    fun initMessageGroups() = totalRepository.checkMessageGroupAtFirst()

//    fun getGroupList():ArrayList<MessageGroupData> = totalRepository.getMessageGroupList()

    //delete Message
    fun deleteMessage(keyValue:String) = totalRepository.deleteMessage(keyValue)
}