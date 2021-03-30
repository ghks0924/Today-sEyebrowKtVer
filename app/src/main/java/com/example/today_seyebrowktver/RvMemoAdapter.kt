package com.example.today_seyebrowktver

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.today_seyebrowktver.databinding.RvItemMemoBinding

class RvMemoAdapter: RecyclerView.Adapter<RvMemoAdapter.ViewHolder> {

    var data: ArrayList<MemoData>? = null

    constructor(data: ArrayList<MemoData>){
        this.data = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvMemoAdapter.ViewHolder {
        val binding: RvItemMemoBinding =
                RvItemMemoBinding.inflate(LayoutInflater.from(parent.context), parent,
                        false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RvMemoAdapter.ViewHolder, position: Int) {
        holder.bind(data!![position])
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    class ViewHolder(binding: RvItemMemoBinding) : RecyclerView.ViewHolder(binding.root){
        var binding: RvItemMemoBinding

        fun bind(memoData: MemoData){
//            binding.memoTitle.text = memoData.memoTitle
//            binding.memoDate.text = memoData.memoDate
//            binding.memoContent.text = memoData.memoContent

            binding.memoTitle.text = "제목"
            binding.memoDate.text = "시간"
            binding.memoContent.text = "내용"

        }

        init {
            this.binding = binding
        }
    }

}