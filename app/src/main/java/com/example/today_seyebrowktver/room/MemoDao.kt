package com.example.today_seyebrowktver.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.today_seyebrowktver.data.MemoData

@Dao
interface MemoDao {

    @Query("SELECT * FROM memos ORDER BY date DESC")
    fun getAllMemos(): LiveData<List<MemoData>>


    @Query("SELECT * FROM memos WHERE date LIKE :date")
    fun findByDate(date: String) : MemoData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(memoEntity : MemoData)


//    @Query("UPDATE memos set date = :date, title = :title, content = :content WHERE idx = :idx")
//    fun update(idx: Int, date : String, title : String?, content : String)

//    @Query("DELETE FROM memos WHERE idx = :idx")
//    fun delete(idx: Int)

    @Update
    fun update(memoEntity: MemoData)

    @Delete
    fun delete(memoEntity: MemoData)

}