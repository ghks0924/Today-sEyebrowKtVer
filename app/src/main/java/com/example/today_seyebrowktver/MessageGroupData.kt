package com.example.today_seyebrowktver

import androidx.annotation.Keep

@Keep
class MessageGroupData (
    var groupName: String = "",
    var numberOfMessages : String = "",
    var order : Int = 0,
    var savedate : String = "",
    var keyValue : String = "",
    var deleteCheck : Boolean = false
    )