package com.example.today_seyebrowktver

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.today_seyebrowktver.databinding.RvItemCustomersBinding

class RvCustomerAdapter : RecyclerView.Adapter<RvCustomerAdapter.ViewHolder> {


    var data: ArrayList<CustomersData>? = null

    constructor(data: ArrayList<CustomersData>) {
        this.data = data
    }

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    interface ItemLongClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null
    var itemLongClick: ItemLongClick? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RvCustomerAdapter.ViewHolder {
        val binding: RvItemCustomersBinding =
            RvItemCustomersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: com.example.today_seyebrowktver.RvCustomerAdapter.ViewHolder, position: Int, ) {
        holder.bind(data!![position])

        //itemClick event
        if (itemClick != null){
            holder?.binding.parentCardview.setOnClickListener{
                itemClick?.onClick(it, position)
            }
        }

        //itemLongClick event
        if (itemLongClick != null){
            holder?.binding.parentCardview.setOnLongClickListener(View.OnLongClickListener {
                itemLongClick?.onClick(it, position)
                return@OnLongClickListener true
            })
        }

    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    class ViewHolder(binding: RvItemCustomersBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: RvItemCustomersBinding

        fun bind(customerData: CustomersData) {
            binding.customerName.text = customerData.customerName
            binding.customerNumber.text = customerData.customerNumber
            binding.customerHistory.text = customerData.history.toString() + "회 방문"
        }

        init {
            this.binding = binding
        }
    }
}