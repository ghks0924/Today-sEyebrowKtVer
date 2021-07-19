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
import com.example.today_seyebrowktver.databinding.FragmentMemoBinding
import com.example.today_seyebrowktver.viewmodel.FragmentMemoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

private val TAG = "FragmentMemo"
class FragmentMemo : Fragment() {

    private val REQUEST_CREATE_MEMO: Int = 1111
    private val REQUEST_UPDATE_MEMO: Int = 2222

//    //viewBinding
//    private var _binding: FragmentMemoBinding? = null //onDestory를 위한 변수
//    private val binding get() = _binding!!

    //dataBinding
    private var mBinding: FragmentMemoBinding? = null

    //RecyclerView를 위한 변수들
    var memoDataList = ArrayList<MemoData>(emptyList())
    var adapter: RvMemoAdapter? = RvMemoAdapter(emptyList())

    //viewModel
    private val fragmentMemoViewModel: FragmentMemoViewModel by lazy {
        ViewModelProvider(this).get(FragmentMemoViewModel::class.java)
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
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        mainViewModel = ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreate")

        setLayout()

        //현재 memoDB에서 가지고 있는 memos 가져오기
        fragmentMemoViewModel.memoListLiveData.observe(
            viewLifecycleOwner,
            Observer { memos ->
                memos?.let {
                    Log.d(TAG, "Got memos ${memos.size}")
                    memoDataList.clear()
                    memoDataList.addAll(memos)
                    updateUI(memos)
                }
            }
        )



    }

    private fun setLayout() {
        createMemo()
    }

    private fun createMemo() {
        mBinding!!.fab.setOnClickListener(View.OnClickListener {

            (activity as ActivityMain).mGoToCreateMemoActivity()

//            lifecycleScope.launch(Dispatchers.IO) {
//                fragmentMemoViewModel.addMemo(
//                    MemoData("2021-07-19",
//                        "test_Title",
//                        "test_Content",
//                        UUID.randomUUID().toString())
//                )
//            }
//            Log.d(TAG, "memo Created?")

        })
    }//memo 추가 메서드



    //ViewModel로 RV 데이터 불러오기

    private fun updateUI(memos:List<MemoData>){
        adapter = RvMemoAdapter(memos)
        mBinding!!.recyclerview.adapter = adapter
        setItemClickListeners()
    }

    private fun setItemClickListeners() {
        //adapter의 itemClick의 onClick override
        adapter!!.itemClick = object : RvMemoAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                Log.d(TAG, memoDataList[position].memoId + ":" + memoDataList[position].memoTitle)

                val selectedMemo = memoDataList[position]

                val intent = Intent(requireActivity(), ActivityUpdateMemo::class.java)
                intent.putExtra("title", selectedMemo.memoTitle)
                intent.putExtra("content", selectedMemo.memoContent)
                intent.putExtra("date", selectedMemo.memoDate)
                intent.putExtra("id", selectedMemo.memoId)
                startActivity(intent)

//                (activity as ActivityMain).mGoToUpdateMemoActivity()

            }


        }

        //adapter의 itemLongClick의 onLongClick override
        adapter!!.itemLongClick = object : RvMemoAdapter.ItemLongClick {
            override fun onLongClick(view: View, position: Int) {

                val ab: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
                ab.setMessage("해당 메모를 삭제 하시겠습니까?")
                ab.setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->

                    //memo 삭제
                        fragmentMemoViewModel.deleteMemo(memoDataList[position])


                })
                ab.setNegativeButton("아니오", DialogInterface.OnClickListener { dialog, which -> })
                ab.setCancelable(true)
                ab.show()
            }
        }

    }

    companion object {
        fun newInstance(): FragmentMemo{
            return FragmentMemo()
        }
    }
}