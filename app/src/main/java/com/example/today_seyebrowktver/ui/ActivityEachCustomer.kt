package com.example.today_seyebrowktver.ui

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.today_seyebrowktver.CustomersData
import com.example.today_seyebrowktver.PhotoData
import com.example.today_seyebrowktver.RvPhotoAdapter
import com.example.today_seyebrowktver.databinding.ActivityEachCustomerBinding
import com.google.firebase.database.*
import java.util.*


class ActivityEachCustomer : ActivityBase() {

    val REQUEST_EDIT = 1111;

    private lateinit var binding: ActivityEachCustomerBinding

    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var uid: String

    private lateinit var customerKeyValue: String
    private lateinit var eachCustomer: CustomersData


    //Photo RecyclerView를 위한 변수들
    private var photoDataList = ArrayList<PhotoData>()
    private var adapter: RvPhotoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEachCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        uid = mAuth.currentUser!!.uid

        val intent = intent
        customerKeyValue = intent.getStringExtra("keyValue")

        getCustomerData()
        mSetPhotoData()

    }

    private fun mSetPhotoData() {
        //서버에서 섬네일이랑 실제 경로 구해오기

        //Test code

        for (i in 0 until 10){
            photoDataList.add(PhotoData("www.naver.com"))
        }

        setRv()
    }

    private fun setRv() {
        adapter = RvPhotoAdapter(photoDataList)

        //itemClick event
        adapter!!.itemClick = object : RvPhotoAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                mShowShortToast("클릭")
            }
        }

        //itemLongClick event
        adapter!!.itemLongClick = object : RvPhotoAdapter.ItemLongClick {
            override fun onLongClick(view: View, position: Int) {
            }
        }

        binding.photoRv.layoutManager = GridLayoutManager(applicationContext,4)
        binding.photoRv.adapter = adapter
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


        binding.backIv.setOnClickListener {
            finish()
        }

        binding.customerNumberTv.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.customerNumberTv.setOnClickListener {
            ShowAlertDialogWithListview()
        }

        binding.editIcon.setOnClickListener {
            val intent = Intent(this, ActivityEditCustomer::class.java)
            intent.putExtra("keyValue", customerKeyValue)
            startActivityForResult(intent, REQUEST_EDIT)

        }

        binding.fixedMemoLayout.setOnClickListener {

            if (binding.memoEt.visibility == View.VISIBLE){
//                TransitionManager.beginDelayedTransition(binding.memoCardview,
//                    AutoTransition())
                binding.memoEt.visibility = View.GONE
                binding.memoExpandIv.setImageResource(com.example.today_seyebrowktver.R.drawable.outline_expand_more_black_36)
                updateCustomerMemo()
            } else{
//                TransitionManager.beginDelayedTransition(binding.memoCardview,
//                    AutoTransition())
                binding.memoEt.visibility = View.VISIBLE
                binding.memoExpandIv.setImageResource(com.example.today_seyebrowktver.R.drawable.outline_expand_less_black_36)
                binding.memoEt.setText(eachCustomer.customerMemo)
                binding.memoEt.requestFocus()
                binding.memoEt.setSelection(eachCustomer.customerMemo.length)
            }

        }

        binding.historyFixedLayout.setOnClickListener {
            if (binding.hidenView.visibility == View.VISIBLE){
//                TransitionManager.beginDelayedTransition(binding.historyCardview,
//                    AutoTransition())
                binding.hidenView.visibility = View.GONE
                binding.historyExpandIv.setImageResource(com.example.today_seyebrowktver.R.drawable.outline_expand_more_black_36)
            } else{
//                TransitionManager.beginDelayedTransition(binding.historyCardview,
//                    AutoTransition())
                binding.hidenView.visibility = View.VISIBLE
                binding.historyExpandIv.setImageResource(com.example.today_seyebrowktver.R.drawable.outline_expand_less_black_36)
            }
        }
    }

    fun updateCustomerMemo(){
        database.child("users").child(uid).child("customers").child(customerKeyValue)
            .child("customerMemo").setValue(binding.memoEt.text.toString().trim()).addOnSuccessListener {
            }.addOnFailureListener {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK){
            return
        }

        when(requestCode){
            REQUEST_EDIT -> {
                getCustomerData()
                binding.customerNameTv.text = data!!.getStringExtra("edittedName")
                binding.customerNumberTv.text = data!!.getStringExtra("edittedNumber")
            }
        }


    }


}