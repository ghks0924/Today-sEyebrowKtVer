package com.example.today_seyebrowktver

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.today_seyebrowktver.databinding.RvItemCustomersBinding

class RvCustomerAdapter : RecyclerView.Adapter<RvCustomerAdapter.ViewHolder> {

    var data: ArrayList<CustomersData>? = null

    constructor(data: ArrayList<CustomersData>) {
        this.data = data
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): RvCustomerAdapter.ViewHolder {
        val binding: RvItemCustomersBinding =
                RvItemCustomersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
            holder: com.example.today_seyebrowktver.RvCustomerAdapter.ViewHolder,
            position: Int
    ) {
        holder.bind(data!![position])
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    class ViewHolder(binding: RvItemCustomersBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: RvItemCustomersBinding

        fun bind(customerData: CustomersData) {
            binding.customerName.text = "김순자"
            binding.customerNumber.text = "01030773637"
            binding.customerHistory.text = "1회"
        }

        init {
            this.binding = binding
        }
    }
}