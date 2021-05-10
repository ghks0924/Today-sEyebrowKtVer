package com.example.today_seyebrowktver

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.today_seyebrowktver.databinding.FragmentCustomersBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class FragmentCustomers : Fragment() {

    private var _binding: FragmentCustomersBinding? = null //onDestory를 위한 변수

    private val binding get() = _binding!!

    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    val mAuth = FirebaseAuth.getInstance()
    var data = ArrayList<CustomersData>()
    var dataForSearch = ArrayList<CustomersData>()
    var adapter: RvCustomerAdapter? = null

    private lateinit var uid: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentCustomersBinding.inflate(inflater, container, false)

        val user = mAuth.currentUser
        uid = user.uid

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData() //customer 데이터 받아와서 arrayList에 넣기
        setLayout() //화면요소 클릭리스너 등 세팅
    }

    private fun setLayout() {
        binding.abcTv.setOnClickListener(
            View.OnClickListener
            //가나다순 버튼
            {
                binding.abcTv.setTextColor(Color.parseColor("#4f4f4f"))
                binding.savedTv.setTextColor(Color.parseColor("#4D4f4f4f"))
            })

        binding.savedTv.setOnClickListener(View.OnClickListener
        //저장순
        {
            binding.abcTv.setTextColor(Color.parseColor("#4D4f4f4f"))
            binding.savedTv.setTextColor(Color.parseColor("#4f4f4f"))

        })

        binding.edittext.clearFocus()

        binding.fab.setOnClickListener(View.OnClickListener {

            (activity as ActivityMain).mSelectHowToCreateCustomer()

        })

        binding.edittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                var searchText: String = binding.edittext.text.toString().toLowerCase()
                search(searchText)

                if (searchText.isEmpty()) {
                    setClickListener(data)
                } else {
                    setClickListener(dataForSearch)
                }

            }

        })
    }

    private fun setData() {
        database.child("users").child(uid).child("customers")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val newData: ArrayList<CustomersData> = ArrayList()
                    for (ds in dataSnapshot.children) {
                        val customersData: CustomersData? = ds.getValue(CustomersData::class.java)
                        if (customersData != null) {
                            newData.add(customersData)
                        }
                    }
                    data.clear() //for문이 끝내기전까지 데이터를 유지하기 위해
                    dataForSearch.clear()

                    data.addAll(newData)
                    dataForSearch.addAll(newData)
                    setRv() //일반적인 위치는 아님..  db접근, 파일접근은 비동기처리 해야함. fb는 자동적으로 비동기적으로 돈다
                }


                //addListener sing은 한번만 불러오고
                //addValue는 데이터가 바꿀때마다 datachage 돈다.
                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    private fun setRv() {
        adapter = RvCustomerAdapter(data)
        binding.recyclerview.layoutManager = LinearLayoutManager(context)
        binding.recyclerview.adapter = adapter

        val dividerDecoration: DividerItemDecoration =
            DividerItemDecoration(
                binding.recyclerview.context,
                LinearLayoutManager(context).orientation
            )

        binding.recyclerview.addItemDecoration(dividerDecoration)

        setClickListener(data)

//        val decoration_height = RecyclerDecorationHeight(5)
//        binding.recyclerview.addItemDecoration(decoration_height)

    }

    private fun setClickListener(data: ArrayList<CustomersData>) {
        adapter!!.itemClick = object : RvCustomerAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(context, ActivityEachCustomer::class.java)
                intent.putExtra("keyValue", data[position].keyValue)
                startActivity(intent)

                Log.d("click?", "click OK" + data[position].keyValue)

            }

        }

        adapter!!.itemLongClick = object : RvCustomerAdapter.ItemLongClick {
            override fun onClick(view: View, position: Int) {
                Log.d("click?", "LongClick OK")
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                    || data.get(i).customerNumber!!.contains(charText)
                ) {
                    //검색된 데이터를 리스트에 추가한다.
                    //list.add(arraylist.get(i));
                    dataForSearch.add(data.get(i))
                }
            }


            adapter = RvCustomerAdapter(dataForSearch)
            binding.recyclerview.adapter = adapter
        }

    }


}