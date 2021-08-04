package com.example.today_seyebrowktver

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.today_seyebrowktver.data.CustomersData
import com.example.today_seyebrowktver.data.MemoData
import com.example.today_seyebrowktver.room.MemoDatabase
import com.example.today_seyebrowktver.ui.ActivityCreateEventNewCus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

private const val MEMO_DATABASE = "memo-database"
private const val TAG = "Total Respository"

class TotalRepository private constructor(context: Context) {

    // firebase Auth
    val mAuth = FirebaseAuth.getInstance()
    val uid:String = mAuth.uid.toString()

    //firebase Realtime database
    val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    //Customer Functions
    fun getCustomersList() : ArrayList<CustomersData>{
        val customersList: ArrayList<CustomersData> = ArrayList()
        database.child("users").child(uid).child("customers")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (ds in dataSnapshot.children) {
                        val customersData: CustomersData? = ds.getValue(CustomersData::class.java)
                        if (customersData != null) {
                            customersList.add(customersData)
                        }
                    }

                    database.child("users").child(uid).child("customersSort").get()
                        .addOnSuccessListener {
                            val customersSort: String = it.value.toString()

                            if (customersSort == "name"){
                                customersList.sortBy { data1 -> data1.customerName }
                            } else{
                                customersList.sortByDescending { data1 -> data1.savedate }
                            }

                        }



//                    setRv() //일반적인 위치는 아님..  db접근, 파일접근은 비동기처리 해야함. fb는 자동적으로 비동기적으로 돈다
                }


                //addListener sing은 한번만 불러오고
                //addValue는 데이터가 바꿀때마다 datachage 돈다.
                override fun onCancelled(databaseError: DatabaseError) {}
            })
        return customersList
    }

    //Message Functions
    fun getMessageGroupList() : ArrayList<MessageGroupData>{
        val messageGroupList: ArrayList<MessageGroupData> = ArrayList()
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


                    if (messageGroupList.isNullOrEmpty()){//초기 실행시 messageGroupList가 비어있으면 생성
                        mInitMessageGroupList()
                    } else {

                    }

                    //정렬
                    messageGroupList.sortBy {it.order}


                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("messageGroup", error.message)
                }


            })

        return messageGroupList
    }

    fun mInitMessageGroupList(){
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

        val reservGroup = MessageGroupData("예약안내","0",0,saveDate,keyForReserv.toString(),false)
        val retouchGroup = MessageGroupData("리터치","0",1,saveDate,keyForRetouch.toString(),false)
        val afterGroup = MessageGroupData("시술후","0",2,saveDate,keyForAfter.toString(),false)
        val etcGroup = MessageGroupData("기타","0",3,saveDate,keyForEtc.toString(),false)


        var groupMap = HashMap<String, MessageGroupData>()
        groupMap.put(keyForReserv.toString(), reservGroup)
        groupMap.put(keyForRetouch.toString(), retouchGroup)
        groupMap.put(keyForAfter.toString(), afterGroup)
        groupMap.put(keyForEtc.toString(), etcGroup)

        database.child("users").child(uid).child("messageGroups").setValue(groupMap)
            .addOnSuccessListener {
                Log.d(TAG, "messageGroup : init success")

            }.addOnFailureListener {
                Log.d("errorOfCustomerSave", it.message.toString())
            }

    }




    //retrofit2


    //Local DB

    //Background Thread executor
    private val executor = Executors.newSingleThreadExecutor()

    //memo RoomDB
    private val memoDB: MemoDatabase = Room.databaseBuilder(
        context.applicationContext,
        MemoDatabase::class.java,
        MEMO_DATABASE
    ).build()

    private val memoDao = memoDB.getMemeDao()

    fun getMemos(): LiveData<List<MemoData>> = memoDao.getAllMemos()

    fun insertMemo(memo: MemoData) {
        executor.execute {
            memoDao.insert(memo)
        }
    }

    fun deleteMemo(memo: MemoData) {
        executor.execute {
            memoDao.delete(memo)
        }
    }

    fun findMemo(date: String): MemoData {
        val memo = memoDao.findByDate(date)
        return memo
    }

    companion object {
        private var INSTANCE: TotalRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = TotalRepository(context)
            }
        }

        fun get(): TotalRepository {
            return INSTANCE ?: throw IllegalStateException("TotalRepository must be initialized")
        }
    }

}