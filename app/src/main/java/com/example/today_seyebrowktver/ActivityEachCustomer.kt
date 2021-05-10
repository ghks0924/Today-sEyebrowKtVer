package com.example.today_seyebrowktver

import android.R
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.today_seyebrowktver.databinding.ActivityEachCustomerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*


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

        binding.customerNumberTv.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.customerNumberTv.setOnClickListener {
            ShowAlertDialogWithListview()
        }

        binding.editIcon.setOnClickListener {
            val intent = Intent(this, ActivityEditCustomer::class.java)
            intent.putExtra("keyValue", customerKeyValue)
            startActivity(intent)

        }

        binding.fixedLayout.setOnClickListener {
            if (binding.hidenView.visibility == View.VISIBLE){
                TransitionManager.beginDelayedTransition(binding.cardview,
                    AutoTransition())
                binding.hidenView.visibility = View.GONE
                binding.historyExpandIv.setImageResource(R.drawable.arrow_down_float)
            } else{
                TransitionManager.beginDelayedTransition(binding.cardview,
                    AutoTransition())
                binding.hidenView.visibility = View.VISIBLE
                binding.historyExpandIv.setImageResource(R.drawable.arrow_up_float)
            }
        }
    }

    fun ShowAlertDialogWithListview() {
        val numberMethod: MutableList<String> = ArrayList()
        numberMethod.add("전화걸기")
        numberMethod.add("SMS 보내기")
        numberMethod.add("복사")

        //Create sequence of items
        val Animals: Array<String> = numberMethod.toTypedArray()
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle(eachCustomer.customerNumber)
        dialogBuilder.setItems(Animals) { dialog, item ->
            val selectedText = Animals[item].toString() //Selected item in listview
            if (selectedText.startsWith("전화")) {
                val uri = Uri.parse("tel:${eachCustomer.customerNumber}")
                val intent = Intent(Intent.ACTION_DIAL, uri)
                startActivity(intent)
            } else if (selectedText.startsWith("SMS")) {
                val intent = Intent(Intent.ACTION_SENDTO)
                val uri = Uri.parse("sms:${eachCustomer.customerNumber}")
                intent.data = uri
                //                    intent.putExtra("sms_body", customer_number);
                startActivity(intent)
            } else {
                //클립보드에 문자열 저장하기
                val clipboard =
                    getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("number data", eachCustomer.customerNumber)
                clipboard.setPrimaryClip(clipData)
                val toast = Toast.makeText(applicationContext, "복사하였습니다", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.TOP, 0, 500)
                toast.show()
            }
        }
        //Create alert dialog object via builder
        val alertDialogObject = dialogBuilder.create()
        //Show the dialog
        alertDialogObject.show()
    }


}