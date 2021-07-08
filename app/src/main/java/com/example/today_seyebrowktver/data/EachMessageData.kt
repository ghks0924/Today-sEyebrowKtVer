package com.example.today_seyebrowktver.data

import androidx.annotation.Keep

@Keep
class EachMessageData (
    var messageType: String = "",
    var messageTitle : String = "",
    var messageContent : String = "",
    var messageDate : String = "",
    var keyValue : String = ""
    )