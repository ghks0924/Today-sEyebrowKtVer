package com.example.today_seyebrowktver

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MessageDao {

    @Query("SELECT * FROM messages ORDER BY date DESC")
    fun getAllMessages(): LiveData<List<MessageData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(messageEntity : MessageData)

    @Query("SELECT * FROM messages WHERE date LIKE :date")
    suspend fun findByDate(date: String) : MessageData

    @Update
    suspend fun update(messageEntity : MessageData)

    @Delete
    suspend fun delete(messageEntity : MessageData)
}