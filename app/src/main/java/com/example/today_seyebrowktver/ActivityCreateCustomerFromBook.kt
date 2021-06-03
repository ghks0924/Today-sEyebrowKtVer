package com.example.today_seyebrowktver

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.SparseArray
import android.util.SparseBooleanArray
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.today_seyebrowktver.databinding.ActivityCreateCustomerFromBookBinding

class ActivityCreateCustomerFromBook : ActivityBase() {

    //viewBinding
    private lateinit var binding: ActivityCreateCustomerFromBookBinding

    private lateinit var contactUtil: ContactUtil

    companion object{
        private lateinit var contactList: ArrayList<ContactData>
    }

    private lateinit var contactListForSearch: ArrayList<ContactData>
    private lateinit var adapter: RvCustomerBookAdapter

    var isCheckAll = false
    var checkCount = 0
    val mSelectedCustomers = SparseArray<ContactData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCustomerFromBookBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //contact Util 객체 생성
        contactUtil = ContactUtil(this)

        setData()
        setLayout()
    }


    private fun setLayout() {
        //back 버튼
        binding.backIv.setOnClickListener(View.OnClickListener {
            finish()
        })

        binding.selectAllTv.setOnClickListener {
            Log.d("clicked?", "ok")
//            selectAllCustomers()
        }

        //검색기능
        binding.edittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable) {
                var searchText: String = binding.edittext.text.toString().toLowerCase()
                search(searchText)
            }
        })


    }

    //검색 기능 메소드 생성
    fun search(charText: String) {
        //문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        contactListForSearch.clear() // 원본 비워! 왜냐면 Adapter가 원본이랑 연결되어 있거든
        //문자입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length == 0) {
            adapter = RvCustomerBookAdapter(contactList)
            binding.recyclerview.adapter = adapter
        } else { //문자입력시
            for (i in contactList.indices) { //리소스의 모든 데이터를 검색한다.
                if (contactList.get(i).name!!.toLowerCase().contains(charText)
                    || contactList.get(i).phoneNumber!!.contains(charText)) {
                    //검색된 데이터를 리스트에 추가한다.
                    //list.add(arraylist.get(i));
                    contactListForSearch.add(contactList.get(i))
                }
            }

            adapter = RvCustomerBookAdapter(contactListForSearch)
            binding.recyclerview.adapter = adapter
        }
    }


    fun getContacts(): ArrayList<ContactData> {
        val arrayList: ArrayList<ContactData> = contactUtil.getContactList() //일단 받아와서

        if (arrayList!!.size == 0) { //연락처에 아무번호도 저장되어있지 않은경우를 위한 로직
            mShowShortToast("핸드폰의 연락처가 존재하지 않습니다.")
            finish()
        }

        return arrayList
    }

    fun setData() {
        //핸드폰 연락처 가져오기
        contactList = getContacts()
        contactListForSearch = getContacts() //검색을 위해 카피본
        setRv()
    }

    private fun setRv() {
        adapter = RvCustomerBookAdapter(contactList)
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = adapter

        val dividerDecoration: DividerItemDecoration =
            DividerItemDecoration(
                binding.recyclerview.context,
                LinearLayoutManager(this).orientation
            )

        binding.recyclerview.addItemDecoration(dividerDecoration)

        adapter!!.itemClick = object : RvCustomerBookAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {

                if (adapter.mSelectedCustomers.get(position, false)){
                    adapter.mSelectedCustomers.put(position,false)
                    view.setBackgroundColor(Color.WHITE)
                    mSelectedCustomers.delete(position)
                    Log.d("selectedCustomers",mSelectedCustomers.size().toString())
                } else{
                    adapter.mSelectedCustomers.put(position,true)
                    view.setBackgroundColor(Color.parseColor("#50ebbdc5"))
                    mSelectedCustomers.put(position, contactList[position])
                    Log.d("selectedCustomers",mSelectedCustomers.size().toString())
                }
            }
        }
    }

//    fun selectAllCustomers(){
//        var position:Int
//        if (mSelectedCustomers.size() == 0){
//            for (i in 0 until adapter.mSelectedCustomers.size()){
//                position = adapter.mSelectedCustomers.keyAt(i)
//                adapter.mSelectedCustomers.put(position, true)
//                adapter = RvCustomerBookAdapter(contactList)
//            }
//        } else{
//            for (i in 0 until adapter.mSelectedCustomers.size()){
//                position = adapter.mSelectedCustomers.keyAt(i)
//                adapter.mSelectedCustomers.put(position, false)
//                adapter = RvCustomerBookAdapter(contactList)
//            }
//        }
//
//    }

}