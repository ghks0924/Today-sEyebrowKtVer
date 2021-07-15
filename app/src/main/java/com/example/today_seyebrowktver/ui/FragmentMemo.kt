package com.example.today_seyebrowktver.ui

import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.today_seyebrowktver.data.MemoData
import com.example.today_seyebrowktver.RvMemoAdapter
import com.example.today_seyebrowktver.databinding.ActivityImagePickerBinding
import com.example.today_seyebrowktver.databinding.FragmentMemoBinding
import com.example.today_seyebrowktver.viewmodel.MemoFragmentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class FragmentMemo : Fragment() {

    private val TAG = "FragmentMemo"

    private val REQUEST_CREATE_MEMO: Int = 1111
    private val REQUEST_UPDATE_MEMO: Int = 2222

//    //viewBinding
//    private var _binding: FragmentMemoBinding? = null //onDestory를 위한 변수
//    private val binding get() = _binding!!

    //dataBinding
    private var mBinding: FragmentMemoBinding? = null

    //RecyclerView를 위한 변수들
    var memoDataList = ArrayList<MemoData>(emptyList())
    var adapter: RvMemoAdapter? = null

    //viewModel
    private val memoFragmentViewModel: MemoFragmentViewModel by lazy {
        ViewModelProvider(this).get(MemoFragmentViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        //dataBinding
        mBinding = FragmentMemoBinding.inflate(inflater)

        mBinding!!.recyclerview.layoutManager = GridLayoutManager(context, 2)
        adapter = RvMemoAdapter(memoDataList) //adapter 생성
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        mainViewModel = ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreate")
        setLayout()
        setData()

    }

    private fun setLayout() {
        createMemo()
    }

    private fun createMemo() {
        mBinding!!.fab.setOnClickListener(View.OnClickListener {
            (activity as ActivityMain).mGoToCreateMemoActivity()
        })
    }//memo 추가 메서드

    private fun setData() {


//        memoFragmentViewModel.getAll().observe(requireActivity(), Observer { memos ->
//            memoDataList.clear()
//            memoDataList.addAll(memos)
//            Log.d(TAG, memos.size.toString()+"?")
            setRv()
//
//        })


    } //ViewModel로 RV 데이터 불러오기

    private fun updateUI(memos:List<MemoData>){
        adapter = RvMemoAdapter(memos)
        mBinding!!.recyclerview.adapter = adapter
    }

    private fun setRv() {
        memoFragmentViewModel.memoListLiveData.observe(
            viewLifecycleOwner,
            Observer { memos ->
                memos?.let{
                    Log.i(TAG, "Got memos ${memos.size}")
                    updateUI(memos)
                }
            }
        )

        //adapter의 itemClick의 onClick override
        adapter!!.itemClick = object : RvMemoAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                Log.d(TAG, "memo : " + memoDataList[position])
                val intent = Intent(requireContext(), ActivityUpdateMemo::class.java)
                intent.putExtra("title", memoDataList[position].memoTitle)
                intent.putExtra("content", memoDataList[position].memoContent)
                intent.putExtra("date", memoDataList[position].memoDate)
                startActivityForResult(intent, REQUEST_CREATE_MEMO)
//                memoFragmentViewModel.sendMemoData(
//                    memoDataList[position].memoTitle,
//                    memoDataList[position].memoContent,
//                    memoDataList[position].memoDate)
//
//                (activity as ActivityMain).mGoToUpdateMemoActivity()
            }


        }

        //adapter의 itemLongClick의 onLongClick override
        adapter!!.itemLongClick = object : RvMemoAdapter.ItemLongClick {
            override fun onLongClick(view: View, position: Int) {

                val ab: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
                ab.setMessage("메모를 삭제 하시겠습니까?")
                ab.setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->

                    //memo 삭제
                    lifecycleScope.launch(Dispatchers.IO) {
                        memoFragmentViewModel.delete(memoDataList[position])
                    }

                })
                ab.setNegativeButton("아니오", DialogInterface.OnClickListener { dialog, which -> })
                ab.setCancelable(true)
                ab.show()
            }
        }



        mBinding!!.recyclerview.adapter = adapter

    } //RecyclerView 세팅 메서드



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK) {
            return
        }

        when (requestCode) {
            REQUEST_CREATE_MEMO -> {
                Log.d(TAG, "activity result")
                lifecycleScope.launch(Dispatchers.IO){
                    Log.d(TAG, "memoCreate working?")
                    val memoData = MemoData(data?.getStringExtra("title").toString(),
                    data?.getStringExtra("content").toString(),
                    data!!.getStringExtra("date").toString(),
                        UUID.randomUUID().toString()
                    )

                    Log.d(TAG, "memoCreate data : " + memoData.toString())

                    memoFragmentViewModel.insert(memoData)
                }
            }

            REQUEST_UPDATE_MEMO -> {
//                //ActivirtCreateMemo에서 넘겨준 데이터 받아오기
//                val memoDate: String = data!!.getStringExtra("memoDate")
//                val prevDate: String = data!!.getStringExtra("prevDate")
//                val memoTitle: String = data!!.getStringExtra("memoTitle")
//                val memoContent: String = data!!.getStringExtra("memoContent")
//
//                Log.d("memoCheck", "fragment : " + memoDate)
//
//                lifecycleScope.launch(Dispatchers.IO) {
//                    val memoData: MemoData = mainViewModel.findMemoByDate(prevDate) //이전 메모 객체 찾기
//                    mainViewModel.delete(memoData) //이전 메모 지우고
//                    mainViewModel.insert(MemoData(memoDate, memoTitle, memoContent)) //새로 생성
//
//                }
//
//                mainViewModel.getAll().observe(activity as ActivityMain, Observer { memos ->
//                    var tempMemoDataList = ArrayList<MemoData>()
//                    for (i in 0 until memos.size) {
//                        tempMemoDataList.add(
//                            MemoData(
//                                memos[i].memoDate,
//                                memos[i].memoTitle,
//                                memos[i].memoContent
//                            )
//                        )
//                    }
//                    memoDataList.clear()
//                    memoDataList.addAll(tempMemoDataList)
//
//                })

            }
        }

    }
}