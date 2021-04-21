package com.example.today_seyebrowktver

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.today_seyebrowktver.databinding.RvItemCustomersBinding
import com.example.today_seyebrowktver.databinding.RvItemRegionsBinding
import com.example.today_seyebrowktver.databinding.RvItemShoptypesBinding

class RvShopTypeAdapter : RecyclerView.Adapter<RvShopTypeAdapter.ViewHolder> {

    interface ItemClick{
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null

    var data: ArrayList<String>?= null

    constructor(data: ArrayList<String>) {
        this.data = data
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): RvShopTypeAdapter.ViewHolder {
        val binding: RvItemShoptypesBinding =
            RvItemShoptypesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: com.example.today_seyebrowktver.RvShopTypeAdapter.ViewHolder,
        position: Int
    ) {
        holder.bind(data!![position])

        //itemClick.onClick 호출
        if (itemClick != null){
            holder?.binding.regionTv.setOnClickListener(View.OnClickListener {
                itemClick?.onClick(it, position)
            })
        }
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    class ViewHolder(binding: RvItemShoptypesBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: RvItemShoptypesBinding

        fun bind(region: String) {
            binding.regionTv.text = region
        }

        init {
            this.binding = binding
        }
    }
}