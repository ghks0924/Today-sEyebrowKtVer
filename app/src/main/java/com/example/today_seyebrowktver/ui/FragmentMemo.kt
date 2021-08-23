package com.example.today_seyebrowktver.ui

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.inflate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.today_seyebrowktver.R
import com.example.today_seyebrowktver.data.MemoData
import com.example.today_seyebrowktver.RvMemoAdapter
import com.example.today_seyebrowktver.data.CustomersData
import com.example.today_seyebrowktver.databinding.FragmentMemoBinding
import com.example.today_seyebrowktver.databinding.RvItemCustomersBinding
import com.example.today_seyebrowktver.databinding.RvItemMemoBinding
import com.example.today_seyebrowktver.viewmodel.FragmentMemoViewModel
import kotlin.collections.ArrayList

private val TAG = "FragmentMemo"
class FragmentMemo : Fragment() {

    private val REQUEST_CREATE_MEMO: Int = 1111
    private val REQUEST_UPDATE_MEMO: Int = 2222

//    //viewBinding
//    private var _binding: FragmentMemoBinding? = null //onDestory를 위한 변수
//    private val binding get() = _binding!!

    //dataBinding
    private lateinit var binding: FragmentMemoBinding

    //RecyclerView를 위한 변수들
    var memoDataList = ArrayList<MemoData>(emptyList())
    var adapter: RvMemoAdapter? = RvMemoAdapter(emptyList())

    //viewModel
    private val fragmentMemoViewModel: FragmentMemoViewModel by lazy {
        ViewModelProvider(this).get(FragmentMemoViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        //dataBinding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_memo,
        container, false)

        return binding.root
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
        binding.fab.setOnClickListener(View.OnClickListener {
            (activity as ActivityMain).mGoToCreateMemoActivity()
        })
    }//memo 추가 메서드



    //ViewModel로 RV 데이터 불러오기

    private fun updateUI(memos:List<MemoData>){
        binding.recyclerview.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = MemoAdapter(memos)
        }

//        setItemClickListeners()
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

    private inner class MemoHolder(private val binding: RvItemMemoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.viewModel = MemoViewModel()
        }

        fun bind(memo: MemoData){
            binding.apply {
                viewModel?.memo = memo
                executePendingBindings()
            }
            binding.memoItemCardview.setOnClickListener {
                Log.d(TAG, memo.memoTitle)
                val intent = Intent(requireActivity(), ActivityUpdateMemo::class.java)
                intent.putExtra("title", memo.memoTitle)
                intent.putExtra("content", memo.memoContent)
                intent.putExtra("date", memo.memoDate)
                intent.putExtra("id", memo.memoId)
                startActivity(intent)


            }

            binding.memoItemCardview.setOnLongClickListener(View.OnLongClickListener {
                Log.d(TAG, "Long : " +memo.memoTitle)
                val ab: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
                ab.setMessage("해당 메모를 삭제 하시겠습니까?")
                ab.setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                    //memo 삭제
                    fragmentMemoViewModel.deleteMemo(memo)

                })
                ab.setNegativeButton("아니오", DialogInterface.OnClickListener { dialog, which -> })
                ab.setCancelable(true)
                ab.show()
                return@OnLongClickListener true
            })
        }

    }

    private inner class MemoAdapter(private val memos: List<MemoData>) :
        RecyclerView.Adapter<MemoHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
                MemoHolder {
            val binding = DataBindingUtil.inflate<RvItemMemoBinding>(
                layoutInflater,
                R.layout.rv_item_memo,
                parent,
                false
            )
            return MemoHolder(binding)

        }

        override fun onBindViewHolder(holder: MemoHolder, position: Int) {
            val memo = memos[position]
            holder.bind(memo)
        }

        override fun getItemCount(): Int = memos.size

    }

    companion object {
        fun newInstance(): FragmentMemo{
            val args = Bundle()

            val fragment = FragmentMemo()
            fragment.arguments = args
            return fragment
        }
    }
}