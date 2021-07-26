package com.example.today_seyebrowktver

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.today_seyebrowktver.data.CustomersData
import com.example.today_seyebrowktver.data.MemoData
import com.example.today_seyebrowktver.room.MemoDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.concurrent.Executors

private const val MEMO_DATABASE = "memo-database"

class TotalRepository private constructor(context: Context) {

    //firebase database
    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    val mAuth = FirebaseAuth.getInstance()
    val uid:String = mAuth.uid.toString()

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