package com.example.today_seyebrowktver.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.room.Room
import com.example.today_seyebrowktver.TotalRepository
import com.example.today_seyebrowktver.data.EventData
import com.example.today_seyebrowktver.data.MemoData
import com.example.today_seyebrowktver.data.MessageData
import com.example.today_seyebrowktver.room.EventDatabase
import com.example.today_seyebrowktver.room.MemoDatabase
import com.example.today_seyebrowktver.room.MessageDatabase
import com.google.firebase.auth.FirebaseAuth

class ActivityMainViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var eventsMapByDate:HashMap<String, MutableList<EventData>>

    lateinit var eventsList:LiveData<List<EventData>>
    lateinit var eventsMap:LiveData<HashMap<String, MutableList<EventData>>>

    private val messageDB = Room.databaseBuilder(
        application,
        MessageDatabase::class.java, "messages"
    ).build()

    private val eventsDB = Room.databaseBuilder(
        application,
        EventDatabase::class.java, "events"
    ).build()

    private val memoDB = Room.databaseBuilder(
        application,
        MemoDatabase::class.java, "memos"
    ).build()



    //==========user Data==========
    val mAuth = FirebaseAuth.getInstance()




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

    //=================================event db method========================
    val eventDate = MutableLiveData<String>()
    val eventTime = MutableLiveData<String>()
    val eventComplete = MutableLiveData<Boolean>()
    val eventCustomerName = MutableLiveData<String>()
    val eventCustomerNumber = MutableLiveData<String>()
    val eventCustomerGrade = MutableLiveData<String>()
    val eventIsRetouch = MutableLiveData<Boolean>()
    val eventMenu = MutableLiveData<String>()
    val eventPrice = MutableLiveData<Int>()
    val eventPayment = MutableLiveData<String>()
    val eventReservMemo = MutableLiveData<String>()
    val eventSavedate = MutableLiveData<String>()
    val eventKeyValue = MutableLiveData<String>()

    fun sendEventData(
        date: String, time: String, complete: Boolean, customerName: String,
        customerNumber: String, customerGrade: String, isRetouch: Boolean, menu: String,
        price: Int, payment: String, memo:String, savedate: String, keyValue: String,
    ) {
        eventDate.value = date
        eventTime.value = time
        eventComplete.value = complete
        eventCustomerName.value = customerName
        eventCustomerNumber.value = customerNumber
        eventCustomerGrade.value = customerGrade
        eventIsRetouch.value = isRetouch
        eventMenu.value = menu
        eventPrice.value = price
        eventPayment.value = payment
        eventReservMemo.value = memo
        eventSavedate.value = savedate
        eventKeyValue.value = keyValue
    }

    fun getAllEvents(){
        if (eventsDB.getEventDao().getAllEvents() != null){
            eventsList = eventsDB.getEventDao().getAllEvents()
        }
    }

    fun convertToMap() {
        if (getAllEvents() != null){
            eventsMap = Transformations.map(eventsList) {
                it.groupByTo(HashMap(), {
                    it.date
                })
            }
        }
    }

//    fun getAllEvents(): HashMap<String,MutableList<EventData>>{
//        var dataList = eventsDB.getEventDao().getAllEvents()
//
//        var dataMap : HashMap<String, MutableList<EventData>> = HashMap()
//        Transformations.map(dataList!!){
//            dataMap = it.groupByTo(HashMap(), {
//                it.date
//            })
//
//        }
//
//        return dataMap
//    }

    suspend fun insert(eventData : EventData){
        return eventsDB.getEventDao().insert(eventData)
    }


    //repository version
    private val totalRepository = TotalRepository.get()

}