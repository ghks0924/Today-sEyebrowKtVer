package com.example.today_seyebrowktver

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.today_seyebrowktver.databinding.FragmentMemoBinding
import com.google.firebase.database.*

class FragmentMemo:Fragment() {

    private var _binding: FragmentMemoBinding? = null //onDestory를 위한 변수
    private val binding get() = _binding!!

    //RecyclerView를 위한 변수들

    val db: DatabaseReference = FirebaseDatabase.getInstance().reference
    var data = ArrayList<MemoData>()
    var adapter: RvMemoAdapter?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMemoBinding.inflate(inflater, container, false)
        Log.d("lifecycle Check", "memo onCreateView")

        setData()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("lifecycle Check", "memo destroyView")
    }

    private fun setRv(){
        adapter = RvMemoAdapter(data)
        binding.recyclerview.layoutManager = LinearLayoutManager(context)
        binding.recyclerview.adapter = adapter
    }

    private fun setData() {
        for (index in 1 until 5) {
            data.add(MemoData("가나다", "01030445454", "내용"))
        }

        setRv()

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
}