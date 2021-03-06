package com.example.today_seyebrowktver

import android.content.Context
import android.provider.ContactsContract
import com.example.today_seyebrowktver.data.ContactData
import kotlin.collections.ArrayList

class ContactUtil {

    private var context: Context? = null

    constructor(context: Context) {
        this.context = context
    }

    fun getContactList(): ArrayList<ContactData> {
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,  // 연락처 ID -> 사진 정보 가져오는데 사용
            ContactsContract.CommonDataKinds.Phone.NUMBER,  // 연락처
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        ) // 연락처 이름.
        val selectionArgs: Array<String>? = null
        val sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC"
        val contactCursor =
            context!!.contentResolver.query(uri, projection, null, selectionArgs, sort)
        val contactlist: ArrayList<ContactData> = ArrayList<ContactData>()
        if (contactCursor!!.moveToFirst()) {
            do {
                val contact = ContactData()
                contact.id = contactCursor.getLong(0)
                contact.phoneNumber = contactCursor.getString(1)
                contact.name = contactCursor.getString(2)
                if (!contact.phoneNumber.toString().startsWith("01")) {
                    contactCursor.moveToNext()
                } else {
                    contactlist.add(contact)
                }


            } while (contactCursor.moveToNext())
        }
        return contactlist
    }
}