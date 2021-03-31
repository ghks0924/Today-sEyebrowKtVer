package com.example.today_seyebrowktver

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.today_seyebrowktver.databinding.FragmentMemoBinding
import com.google.firebase.database.*

class FragmentMemo:Fragment() {

    private val REQUEST_CREATE_MEMO: Int = 1111

    //viewBinding
    private var _binding: FragmentMemoBinding? = null //onDestory를 위한 변수
    private val binding get() = _binding!!

    //RecyclerView를 위한 변수들

//    val db: DatabaseReference = FirebaseDatabase.getInstance().reference //firebase db
    var memoDataList = ArrayList<MemoData2>()
    var adapter: RvMemoAdapter?= null

    private lateinit var mainViewModel: ViewModelMain


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMemoBinding.inflate(inflater, container, false)


//        setData()

        mainViewModel = ViewModelProvider(activity as ActivityMain)[ViewModelMain::class.java]


        mainViewModel.getAll().observe(activity as ActivityMain, Observer { memos ->
            var tempMemoDataList = ArrayList<MemoData2>()
            for (i in 0 until memos.size){
                tempMemoDataList.add(MemoData2(memos[i].memoDate, memos[i].memoTitle, memos[i].memoContent))
            }
            memoDataList.clear()
            memoDataList.addAll(tempMemoDataList)

            setRv()
//                    binding.tv.text = memos.toString()
        })

        //memo 추가 메서드
        binding.addMemoCardview.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, ActivityCreateMemo::class.java)
            startActivityForResult(intent, 1111)
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setRv(){
        adapter = RvMemoAdapter(memoDataList)
        binding.recyclerview.layoutManager = LinearLayoutManager(context)
        binding.recyclerview.adapter = adapter
    }

    private fun setData() {
////        for (index in 1 until 5) {
////            data.add(MemoData("가나다", "01030445454", "내용"))
////        }
////
////        setRv()
//
//        db.child("memos").addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val newData: ArrayList<MemoData> = ArrayList()
//                for (ds in dataSnapshot.children) {
//                    val memoData: MemoData? = ds.getValue(MemoData::class.java)
//                    if (memoData != null) {
//                        newData.add(memoData)
//                    }
//                }
//                data.clear()
//                data.addAll(newData)
//                setRv()
//            }
//
//            //addListener
//            override fun onCancelled(databaseError: DatabaseError) {}
//        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK) {
            return
        }

        when (requestCode) {
            REQUEST_CREATE_MEMO -> {
                val memoDate:String = data!!.getStringExtra("memoDate")
                val memoTitle:String = data!!.getStringExtra("memoTitle")
                val memoContent: String = data!!.getStringExtra("memoContent")

                Log.d("getMemo", "memoDate : " + memoDate)
                Log.d("getMemo", "memoTitle : " + memoTitle)
                Log.d("getMemo", "memoContent : " + memoContent)

                mainViewModel.insert(MemoData2(memoDate, memoTitle, memoContent))

                mainViewModel.getAll().observe(activity as ActivityMain, Observer { memos ->
                    var tempMemoDataList = ArrayList<MemoData2>()
                    for (i in 0 until memos.size){
                        tempMemoDataList.add(MemoData2(memos[i].memoDate, memos[i].memoTitle, memos[i].memoContent))
                    }
                    memoDataList.clear()
                    memoDataList.addAll(tempMemoDataList)

                    setRv()
//                    binding.tv.text = memos.toString()
                })


            }
        }

    }
}