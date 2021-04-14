package com.example.today_seyebrowktver

import androidx.annotation.Keep

@Keep
data class ContactData(
    var id: Long? = null,
    var phoneNumber: String? = null,
    var name: String? = null,
    var isChecked : Boolean = false
)