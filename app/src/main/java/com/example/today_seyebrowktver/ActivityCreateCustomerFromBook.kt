package com.example.today_seyebrowktver

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.today_seyebrowktver.databinding.ActivityCreateCustomerFromBookBinding

class ActivityCreateCustomerFromBook : ActivityBase() {

    //viewBinding
    private lateinit var binding: ActivityCreateCustomerFromBookBinding

    private lateinit var contactUtil: ContactUtil
    private lateinit var contactList: ArrayList<ContactData>
    private lateinit var contactListForSearch: ArrayList<ContactData>
    private lateinit var adapter: RvCustomerBookAdapter

    var isCheckAll = false
    var checkCount = 0

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
        binding.backCardview.setOnClickListener(View.OnClickListener {
            finish()
        })

        //all check box
        binding.checkAll.setOnCheckedChangeListener { buttonView, isChecked ->
            when (isChecked) {
                true -> {
                    mShowShortToast("전체 체크됨")
                    isCheckAll = true
                }

                false -> {
                    mShowShortToast("전체 체크 해제됨")
                    isCheckAll = false
                }
            }
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

//        adapter!!.itemClick = object : RvCustomerBookAdapter.ItemClick {
//            override fun onClick(view: View, position: Int) {
//                checkCount++
//            }
//        }
    }

}