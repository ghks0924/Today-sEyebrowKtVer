package com.example.today_seyebrowktver

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.today_seyebrowktver.data.MemoData
import com.example.today_seyebrowktver.data.MessageData
import com.example.today_seyebrowktver.room.MemoDatabase
import com.example.today_seyebrowktver.room.MessageDatabase
import com.google.firebase.auth.FirebaseAuth

class ViewModelMain(application: Application) : AndroidViewModel(application) {

    private val memoDB = Room.databaseBuilder(
        application,
        MemoDatabase::class.java, "memos"
    ).fallbackToDestructiveMigration().build()

    private val messageDB = Room.databaseBuilder(
        application,
        MessageDatabase::class.java, "messages"
    ).build()

    //==========user Data==========
    val mAuth = FirebaseAuth.getInstance()

    //===================Memo db method===================

    val memoTitle = MutableLiveData<String>()
    val memoContent = MutableLiveData<String>()
    val memoDate = MutableLiveData<String>()

    fun sendMemoData(title: String, content: String, date: String) {
        memoTitle.value = title
        memoContent.value = content
        memoDate.value = date
    }

    fun getAll(): LiveData<List<MemoData>> {
        return memoDB.getMemeDao().getAllMemos()
    }

    suspend fun insert(memoData: MemoData) {
        memoDB.getMemeDao().insert(memoData)
    }

    suspend fun delete(memoData: MemoData) {
        memoDB.getMemeDao().delete(memoData)
    }

    suspend fun findMemoByDate(date: String): MemoData {
        val memoData = memoDB.getMemeDao().findByDate(date)
        return memoData
    }

    suspend fun update(memoData: MemoData) {
        memoDB.getMemeDao().update(memoData)
    }


    //===================================Message===================================


    val messageType = MutableLiveData<String>()
    val messageTitle = MutableLiveData<String>()
    val messageContent = MutableLiveData<String>()
    val messageDate = MutableLiveData<String>()


    fun sendMessageData(type: String, title: String, content: String, date: String) {
        messageType.value = type
        messageTitle.value = title
        messageContent.value = content
        messageDate.value = date
    }

    //Message room method
    fun getAllMessages(): LiveData<List<MessageData>> {
        return messageDB.getMessageDao().getAllMessages()
    }

    suspend fun insert(messageData: MessageData) {
        messageDB.getMessageDao().insert(messageData)
    }

    suspend fun findMessageByDate(date: String): MessageData {
        var messageData = messageDB.getMessageDao().findByDate(date)
        return messageData
    }

    suspend fun delete(messageData: MessageData) {
        messageDB.getMessageDao().delete(messageData)
    }

    suspend fun update(messageData: MessageData) {
        messageDB.getMessageDao().update(messageData)
    }


}