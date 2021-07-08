package com.example.today_seyebrowktver.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.today_seyebrowktver.data.EventData
import com.example.today_seyebrowktver.data.MemoData

@Dao
interface EventDao {

    @Query("SELECT * FROM events ORDER BY date DESC")
    fun getAllEvents(): LiveData<List<EventData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(eventEntity : EventData)

//    @Query("SELECT * FROM memos WHERE date LIKE :date")
//    suspend fun findByDate(date: String) : MemoData
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(memoEntity : MemoData)

//    @Query("UPDATE memos set date = :date, title = :title, content = :content WHERE idx = :idx")
//    fun update(idx: Int, date : String, title : String?, content : String)

//    @Query("DELETE FROM memos WHERE idx = :idx")
//    fun delete(idx: Int)

//    @Update
//    suspend fun update(eventEntity: EventData)
//
//    @Delete
//    suspend fun delete(eventEntity: EventData)


}