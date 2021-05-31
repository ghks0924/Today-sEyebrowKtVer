package com.example.today_seyebrowktver

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.today_seyebrowktver.databinding.ActivityLoadCustomersBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ActivityLoadCustomers : ActivityBase() {

    private lateinit var binding: ActivityLoadCustomersBinding
    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var uid : String

    private var data: ArrayList<CustomersData> = ArrayList()
    private var dataForSearch: ArrayList<CustomersData> = ArrayList()
    private lateinit var adapter: RvCustomerAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadCustomersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = mAuth.currentUser
        uid = user.uid

        setData()
        setLayout()
    }

    private fun setLayout() {

        binding.backIv.setOnClickListener {
            finish()
        }

        binding.abcTv.setOnClickListener(
            View.OnClickListener
            //가나다순 버튼
            {
                binding.abcTv.setTextColor(Color.parseColor("#4f4f4f"))
                binding.savedTv.setTextColor(Color.parseColor("#4D4f4f4f"))
                data.sortBy { data1 -> data1.customerName }
                dataForSearch.sortBy { data1 -> data1.customerName }
                adapter.notifyDataSetChanged()

            })

        binding.savedTv.setOnClickListener(
            View.OnClickListener
            //저장순 버튼
            {
                binding.abcTv.setTextColor(Color.parseColor("#4D4f4f4f"))
                binding.savedTv.setTextColor(Color.parseColor("#4f4f4f"))
                data.sortByDescending { data1 -> data1.savedate }
                dataForSearch.sortByDescending { data1 -> data1.savedate }
                adapter.notifyDataSetChanged()
            })

        //검색기능
        binding.edittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                var searchText: String = binding.edittext.text.toString().toLowerCase()
                search(searchText)
            }
        })
    }

    //검색 기능 메소드 생성
    fun search(charText: String) {
        //문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        dataForSearch.clear() // 원본 비워! 왜냐면 Adapter가 원본이랑 연결되어 있거든
        //문자입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length == 0) {
            adapter = RvCustomerAdapter(data)
            binding.recyclerview.adapter = adapter
        } else { //문자입력시
            for (i in data.indices) { //리소스의 모든 데이터를 검색한다.
                if (data.get(i).customerName!!.toLowerCase().contains(charText)
                    || data.get(i).customerNumber!!.contains(charText)) {
                    //검색된 데이터를 리스트에 추가한다.
                    //list.add(arraylist.get(i));
                    dataForSearch.add(data.get(i))
                }
            }

            adapter = RvCustomerAdapter(dataForSearch)
            binding.recyclerview.adapter = adapter
        }
    }

    private fun setData() {
        database.child("users").child(uid).child("customers").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val newData: ArrayList<CustomersData> = ArrayList()
                for (ds in dataSnapshot.children) {
                    val idolData: CustomersData? = ds.getValue(CustomersData::class.java)
                    if (idolData != null) {
                        newData.add(idolData)
                    }
                }
                data.clear() //for문이 끝내기전까지 데이터를 유지하기 위해
                dataForSearch.clear()
                data.addAll(newData)
                dataForSearch.addAll(newData)
                data.sortBy { data1 -> data1.customerName }
                dataForSearch.sortBy { data1 -> data1.customerName }
                setRv() //일반적인 위치는 아님..  db접근, 파일접근은 비동기처리 해야함. fb는 자동적으로 비동기적으로 돈다
            }


            //addListener sing은 한번만 불러오고
            //addValue는 데이터가 바꿀때마다 datachage 돈다.
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun setRv() {
        adapter = RvCustomerAdapter(data)
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = adapter

        val dividerDecoration: DividerItemDecoration =
            DividerItemDecoration(
                binding.recyclerview.context,
                LinearLayoutManager(this).orientation
            )

        binding.recyclerview.addItemDecoration(dividerDecoration)

        setClickListener(data)
    }

    private fun setClickListener(data: ArrayList<CustomersData>) {
        adapter!!.itemClick = object : RvCustomerAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val intent = intent
                intent.putExtra("keyValue", data[position].keyValue)
                intent.putExtra("cusName", data[position].customerName)
                intent.putExtra("cusNumber", data[position].customerNumber)
                intent.putExtra("numOfHistory", data[position].history)

                setResult(RESULT_OK,intent)
                finish()


            }

        }
    }
}