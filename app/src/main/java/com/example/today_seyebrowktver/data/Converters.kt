package com.example.today_seyebrowktver.data

import androidx.room.TypeConverter
import java.util.LinkedHashMap

class Converters {

    @TypeConverter
    fun listToMap(value: List<EventData>?) = value?.groupByTo( HashMap(),{
        it.date })

    fun mapToList(value: HashMap<String,List<EventData>>) = value.values.toList()
}