package com.example.today_seyebrowktver.ui

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import com.example.today_seyebrowktver.BottomSheetFragmentCheckSave
import com.example.today_seyebrowktver.data.CustomersData
import com.example.today_seyebrowktver.databinding.ActivityEditCustomerBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ActivityEditCustomer : ActivityBase() {

    private lateinit var binding:ActivityEditCustomerBinding

    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var uid: String

    private lateinit var customerKeyValue: String
    private lateinit var eachCustomer: CustomersData

    private lateinit var orgName: String
    private lateinit var orgNumber: String


    private var isNameEditted = false
    private var isNumberEditted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        uid = mAuth.currentUser!!.uid

        val intent = intent
        customerKeyValue = intent.getStringExtra("keyValue").toString()

        getCustomerData()


    }

    private fun getCustomerData() {
        database.child("users").child(uid).child("customers").child(customerKeyValue).get()
            .addOnSuccessListener {
                eachCustomer = CustomersData(it.child("customerName").value.toString(),
                    it.child("customerNumber").value.toString(),
                    it.child("customerMemo").value.toString(),
                    it.child("history").value.toString(),
                    it.child("grade").value.toString(),
                    it.child("savedate").value.toString(),
                    Integer.parseInt(it.child("noshowCount").value.toString()),
                    customerKeyValue,
                    Integer.parseInt(it.child("sales").value.toString()),
                    it.child("photoURL").value.toString()
                )

                //수정된 정보와 비교하기 위해서
                orgName = eachCustomer.customerName
                orgNumber = eachCustomer.customerNumber

                setLayout()
            }
    }

    fun setLayout(){
        binding.cancelBtn.setOnClickListener {
            finish()
        }

        binding.saveBtn.setOnClickListener {
            //수정된게 있는지 확인
            if (isAnythingEditted()){ //수정된게 있으면
                //saveData
                saveUpdatedCustomerData()

            } else{
                finish()
            }

        }
        //자동 하이픈 먹이려고
        binding.customerNumberEt.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        binding.customerNameEt.setText(eachCustomer.customerName)
        binding.customerNumberEt.setText(eachCustomer.customerNumber)

        binding.customerNameEt.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isNameEditted = true
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        binding.customerNumberEt.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isNumberEditted = true
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

    }

    private fun isVaildCheck(): Boolean {
        if (binding.customerNameEt.text.isNullOrEmpty()) {
            mShowShortToast("고객이름을 입력해주세요")
            return false
        }

        if (binding.customerNumberEt.text.isNullOrEmpty() || !isValidCellPhoneNumber(binding.customerNumberEt.text.toString())) {
            mShowShortToast("전화번호를 확인해주세요")
            return false
        }
        return true
    }

    //서버 && 이전 액티비티로 데이터 넘김
    fun saveUpdatedCustomerData() {
        if (isVaildCheck()){
            val edittedName = binding.customerNameEt.text.toString().trim()
            val edittedNumber = binding.customerNumberEt.text.toString().trim()

            if (isNameEditted){
                database.child("users").child(uid).child("customers").child(customerKeyValue)
                    .child("customerName").setValue(edittedName).addOnSuccessListener {
                    }.addOnFailureListener {
                    }
            }
            if (isNumberEditted){
                database.child("users").child(uid).child("customers").child(customerKeyValue)
                    .child("customerNumber").setValue(edittedNumber).addOnSuccessListener {
                    }.addOnFailureListener {
                    }
            }


            val intent = intent
            intent.putExtra("edittedName", edittedName)
            intent.putExtra("edittedNumber", edittedNumber)
            setResult(RESULT_OK, intent)
            finish()

        }

    }

    override fun onBackPressed() {
        //수정사항이 있는지 체크
        if (isAnythingEditted()){ //수정사항이 있으면
            val frag = BottomSheetFragmentCheckSave()
            frag.show(supportFragmentManager, frag.tag)
        } else{ //수정사항이 없으면
            finish()
        }

    }

    //수정된 항목이 있는지
    private fun isAnythingEditted() : Boolean{
        if (isNameEditted && orgName != binding.customerNameEt.text.toString().trim()){
            return true
        }
        if (isNumberEditted && orgNumber != binding.customerNumberEt.text.toString().trim()){
            return true
        }
        return false
    }
}