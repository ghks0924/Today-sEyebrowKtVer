package com.example.today_seyebrowktver

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MemoDao2 {

    @Query("SELECT * FROM memos")
    fun getAllMemos(): LiveData<List<MemoData2>>



    @Query("SELECT * FROM memos WHERE idx = :idx ")
    fun findByIdx(idx: Int): MemoData2

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(memoEntity : MemoData2)


//    @Query("UPDATE memos set date = :date, title = :title, content = :content WHERE idx = :idx")
//    fun update(idx: Int, date : String, title : String?, content : String)

//    @Query("DELETE FROM memos WHERE idx = :idx")
//    fun delete(idx: Int)

    @Update
    fun update(memoEntity: MemoData2)

    @Delete
    fun delete(memoEntity: MemoData2)

}