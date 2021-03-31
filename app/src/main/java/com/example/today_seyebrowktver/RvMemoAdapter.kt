package com.example.today_seyebrowktver

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.today_seyebrowktver.databinding.RvItemMemoBinding

class RvMemoAdapter: RecyclerView.Adapter<RvMemoAdapter.ViewHolder> {

    var data: ArrayList<MemoData2>? = null

    constructor(data: ArrayList<MemoData2>){
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

        fun bind(memoData: MemoData2){
//            binding.memoTitle.text = memoData.memoTitle
//            binding.memoDate.text = memoData.memoDate
//            binding.memoContent.text = memoData.memoContent

            binding.memoTitle.text = memoData.memoTitle
            binding.memoDate.text = memoData.memoDate
            binding.memoContent.text = memoData.memoContent

        }

        init {
            this.binding = binding
        }
    }

}