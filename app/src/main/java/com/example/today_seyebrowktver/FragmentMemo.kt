package com.example.today_seyebrowktver

import android.R
import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.today_seyebrowktver.databinding.FragmentMemoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FragmentMemo : Fragment() {

    private val REQUEST_CREATE_MEMO: Int = 1111
    private val REQUEST_UPDATE_MEMO: Int = 2222

    //viewBinding
    private var _binding: FragmentMemoBinding? = null //onDestory를 위한 변수
    private val binding get() = _binding!!

    //RecyclerView를 위한 변수들
    var memoDataList = ArrayList<MemoData>()
    var adapter: RvMemoAdapter? = null

    private lateinit var mainViewModel: ViewModelMain


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMemoBinding.inflate(inflater, container, false)

        setData()

        //memo 추가 메서드
        mCreateMemo()

        return binding.root
    }

    private fun mCreateMemo() {
        binding.addMemoCardview.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, ActivityCreateMemo::class.java)
            intent.putExtra("type", "new")
            startActivityForResult(intent, REQUEST_CREATE_MEMO)
        })
    }//memo 추가 메서드

    private fun setRv() {
        adapter = RvMemoAdapter(memoDataList) //adapter 생성

        //adapter의 itemClick의 onClick override
        adapter!!.itemClick = object : RvMemoAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                var tempMemoDate: String? = null
                var tempMemoTitle: String? = ""
                var tempMemoContent: String? = null

                // 일단 클릭된 놈 찾기
                lifecycleScope.launch(Dispatchers.IO) {
                    val memoData: MemoData =
                        mainViewModel.findByDate(memoDataList[position].memoDate)
                    tempMemoDate = memoData.memoDate
                    tempMemoTitle = memoData.memoTitle.toString()
                    tempMemoContent = memoData.memoContent
                }

                val intent = Intent(context, ActivityCreateMemo::class.java)
                //찾은 놈 데이터 보내주기
                intent.putExtra("type", "update")
                intent.putExtra("memoTitle", tempMemoTitle)
                intent.putExtra("memoDate", tempMemoDate)
                intent.putExtra("memoContent", tempMemoContent)
                startActivityForResult(intent, REQUEST_UPDATE_MEMO)
            }
        }

        //adapter의 itemLongClick의 onLongClick override
        adapter!!.itemLongClick = object : RvMemoAdapter.ItemLongClick {
            override fun onLongClick(view: View, position: Int) {

                var memoData: MemoData

                //일단 클릭된 놈 찾기
                lifecycleScope.launch(Dispatchers.IO) {
                    memoData = mainViewModel.findByDate(memoDataList[position].memoDate)
                }


                val ab: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
                ab.setMessage("메모를 삭제 하시겠습니까??")
                ab.setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->

                    lifecycleScope.launch(Dispatchers.IO) {
                        memoData = mainViewModel.findByDate(memoDataList[position].memoDate)
                        mainViewModel.delete(memoData)
                    }


                })
                ab.setNegativeButton("아니오", DialogInterface.OnClickListener { dialog, which -> })
                ab.setCancelable(true)
                ab.show()
            }

        }


        binding.recyclerview.layoutManager = LinearLayoutManager(context)
        binding.recyclerview.adapter = adapter
    } //RecyclerView 세팅 메서드

    private fun setData() {
        mainViewModel = ViewModelProvider(activity as ActivityMain)[ViewModelMain::class.java]


        mainViewModel.getAll().observe(activity as ActivityMain, Observer { memos ->
            var tempMemoDataList = ArrayList<MemoData>()
            for (i in 0 until memos.size) {
                tempMemoDataList.add(
                    MemoData(
                        memos[i].memoDate,
                        memos[i].memoTitle,
                        memos[i].memoContent
                    )
                )
            }
            memoDataList.clear()
            memoDataList.addAll(tempMemoDataList)

            setRv()

        })


    } //ViewModel로 RV 데이터 불러오기

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK) {
            return
        }

        when (requestCode) {
            REQUEST_CREATE_MEMO -> {
                //ActivirtCreateMemo에서 넘겨준 데이터 받아오기
                val memoDate: String = data!!.getStringExtra("memoDate")
                val memoTitle: String = data!!.getStringExtra("memoTitle")
                val memoContent: String = data!!.getStringExtra("memoContent")

                lifecycleScope.launch(Dispatchers.IO) {
                    mainViewModel.insert(MemoData(memoDate, memoTitle, memoContent))

                }
                mainViewModel.getAll().observe(activity as ActivityMain, Observer { memos ->
                    var tempMemoDataList = ArrayList<MemoData>()
                    for (i in 0 until memos.size) {
                        tempMemoDataList.add(
                            MemoData(
                                memos[i].memoDate,
                                memos[i].memoTitle,
                                memos[i].memoContent
                            )
                        )
                    }
                    memoDataList.clear()
                    memoDataList.addAll(tempMemoDataList)

                })

            }

            REQUEST_UPDATE_MEMO -> {
                //ActivirtCreateMemo에서 넘겨준 데이터 받아오기
                val memoDate: String = data!!.getStringExtra("memoDate")
                val prevDate: String = data!!.getStringExtra("prevDate")
                val memoTitle: String = data!!.getStringExtra("memoTitle")
                val memoContent: String = data!!.getStringExtra("memoContent")

                lifecycleScope.launch(Dispatchers.IO) {
                    val memoData: MemoData = mainViewModel.findByDate(prevDate) //이전 메모 객체 찾기
                    mainViewModel.delete(memoData) //이전 메모 지우고
                    mainViewModel.insert(MemoData(memoDate, memoTitle, memoContent)) //새로 생성

                }

                mainViewModel.getAll().observe(activity as ActivityMain, Observer { memos ->
                    var tempMemoDataList = ArrayList<MemoData>()
                    for (i in 0 until memos.size) {
                        tempMemoDataList.add(
                            MemoData(
                                memos[i].memoDate,
                                memos[i].memoTitle,
                                memos[i].memoContent
                            )
                        )
                    }
                    memoDataList.clear()
                    memoDataList.addAll(tempMemoDataList)

                })

            }
        }

    }
}