package com.example.today_seyebrowktver.data

import androidx.lifecycle.LiveData
import androidx.room.TypeConverter
import java.util.LinkedHashMap

object Converters {

    @TypeConverter
    @JvmStatic
    fun toMap(value: EventData):HashMap<String,EventData> {
        var map : HashMap<String, EventData> = HashMap()
        map.put(value.date, value)
        return map
    }

    @TypeConverter
    @JvmStatic
    fun toEvent(value: HashMap<String,EventData>) : MutableCollection<EventData> {
        return value.values
    }
}