package com.example.today_seyebrowktver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import com.example.today_seyebrowktver.databinding.ActivityEditCustomerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.w3c.dom.Text

class ActivityEditCustomer : ActivityBase() {

    private lateinit var binding:ActivityEditCustomerBinding

    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    val mAuth = FirebaseAuth.getInstance()
    private lateinit var uid: String

    private lateinit var customerKeyValue: String
    private lateinit var eachCustomer: CustomersData

    private var isNameEditted = false
    private var isNumberEditted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        uid = mAuth.currentUser.uid

        val intent = intent
        customerKeyValue = intent.getStringExtra("keyValue")

        getCustomerData()


    }

    private fun getCustomerData() {
        database.child("users").child(uid).child("customers").child(customerKeyValue).get()
            .addOnSuccessListener {
                eachCustomer = CustomersData(it.child("customerName").value.toString(),
                    it.child("customerNumber").value.toString(),
                    it.child("customerMemo").value.toString(),
                    Integer.parseInt(it.child("history").value.toString()),
                    it.child("grade").value.toString(),
                    it.child("savedate").value.toString(),
                    Integer.parseInt(it.child("noshowCount").value.toString()),
                    customerKeyValue,
                    Integer.parseInt(it.child("sales").value.toString()),
                    it.child("photoURL").value.toString()
                )

                setLayout()
            }
    }

    fun setLayout(){
        binding.cancelBtn.setOnClickListener {
            finish()
        }

        binding.saveBtn.setOnClickListener {
            //saveData
            saveUpdatedCustomerData()
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

    private fun saveUpdatedCustomerData() {
        if (isVaildCheck()){
            if (isNameEditted){
                database.child("users").child(uid).child("customers").child(customerKeyValue)
                    .child("customerName").setValue(binding.customerNameEt.text.toString().trim()).addOnSuccessListener {
                    }.addOnFailureListener {
                    }
            }
            if (isNumberEditted){
                database.child("users").child(uid).child("customers").child(customerKeyValue)
                    .child("customerNumber").setValue(binding.customerNumberEt.text.toString().trim()).addOnSuccessListener {
                    }.addOnFailureListener {
                    }
            }


            finish()

        }


    }

    override fun onBackPressed() {
        val frag = BottomSheetFragmentCheckSave()
        frag.show(supportFragmentManager, frag.tag)
    }
}