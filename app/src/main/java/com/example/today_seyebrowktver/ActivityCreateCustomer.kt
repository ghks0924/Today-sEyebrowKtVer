package com.example.today_seyebrowktver

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import com.example.today_seyebrowktver.databinding.ActivityCreateCustomerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class ActivityCreateCustomer : ActivityBase() {


    //viewBinding
    private lateinit var binding: ActivityCreateCustomerBinding

    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    val mAuth = FirebaseAuth.getInstance()
    private lateinit var uid: String


    private lateinit var customerName: String
    private lateinit var customerNumber: String
    private lateinit var customerMemo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = mAuth.currentUser
        uid = user.uid

        setLayout()


    }

    private fun setLayout() {
        //자동 하이픈 먹이려고
        binding.numberEt.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        binding.backCardview.setOnClickListener(View.OnClickListener {
            finish()
        })

        binding.completeBtn.setOnClickListener(View.OnClickListener {
            customerName = binding.nameEt.text.toString().trim()
            //전화번호는 하이픈 빼고 저장하기
            customerNumber = binding.numberEt.text.toString().trim()
            customerMemo = binding.memoEt.text.toString().trim()

            if (isVaildCheck()) {
                customerNumber.replace("-", "")
                mCreateCustomer()
            }

        })
    }

    private fun mCreateCustomer() {

        isVaildCheck()

        // 현재시간을 msec 으로 구한다.
        val now = System.currentTimeMillis()
        // 현재시간을 date 변수에 저장한다.
        val date = Date(now)
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        val sdfNow = SimpleDateFormat("yyyyMMddHHmmss")
        // nowDate 변수에 값을 저장한다.
        val saveDate = sdfNow.format(date)

        val key = database.child("users").child(uid).child("customers").push().key
        val newCustomersData = CustomersData(
            customerName, customerNumber, customerMemo, 0, "default", saveDate, 0, key.toString(),0,""
        )


        database.child("users").child(uid).child("customers").child(key!!).setValue(newCustomersData)
        finish()
        mShowShortToast("고객이 저장되었습니다")

    }

    private fun isVaildCheck(): Boolean {
        if (customerName.isNullOrEmpty()) {
            mShowShortToast("고객이름을 입력해주세요")
            return false
        }

        if (customerNumber.isNullOrEmpty() || !isValidCellPhoneNumber(customerNumber)) {
            mShowShortToast("전화번호를 확인해주세요")
            return false
        }

        return true
    }

}