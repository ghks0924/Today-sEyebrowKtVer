package com.example.today_seyebrowktver

import android.os.Bundle
import android.util.Log
import com.example.today_seyebrowktver.databinding.ActivityEachCustomerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ActivityEachCustomer : ActivityBase() {

    private lateinit var binding: ActivityEachCustomerBinding

    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    val mAuth = FirebaseAuth.getInstance()
    private lateinit var uid: String

    private lateinit var customerKeyValue: String
    private lateinit var eachCustomer: CustomersData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEachCustomerBinding.inflate(layoutInflater)
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


    fun setLayout() {
        binding.customerNameTv.text = eachCustomer.customerName
        binding.customerNumberTv.text = eachCustomer.customerNumber

        binding.backCardview.setOnClickListener {
            finish()
        }
    }
}