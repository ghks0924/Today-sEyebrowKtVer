package com.example.today_seyebrowktver

import androidx.annotation.Keep

@Keep
data class UserData (
    var email : String?= null,
    var password : String?= null,
    var uid : String?= null,
    var shopType : String?= null,
    var shopName : String?= null,
    var number : String?= null,
    var region : String?= null,
    var birth : String?= null,
    var gender : String?= null,
    var svcTerm : String?= null,
    var pvcTerm : String?= null,
    var goal : String?= null,
    var rcmdEmail : String?= null
)