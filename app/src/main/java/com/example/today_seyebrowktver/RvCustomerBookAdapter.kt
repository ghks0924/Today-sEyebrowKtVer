package com.example.today_seyebrowktver

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.today_seyebrowktver.databinding.RvItemCustomersBookBinding

class RvCustomerBookAdapter : RecyclerView.Adapter<RvCustomerBookAdapter.ViewHolder> {

    interface ItemClick{
        fun onClick(view: View, position: Int)
    }

    var data: ArrayList<ContactData>?= null

    constructor(data: ArrayList<ContactData>) {
        this.data = data
    }

    var itemClick: RvCustomerBookAdapter.ItemClick? = null

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): RvCustomerBookAdapter.ViewHolder {
        val binding: RvItemCustomersBookBinding =
            RvItemCustomersBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: com.example.today_seyebrowktver.RvCustomerBookAdapter.ViewHolder,
        position: Int) {
        holder.bind(data!![position])

        //itemClick.onClick 호출
        if (itemClick != null){
            holder?.binding.parentCardview.setOnClickListener(View.OnClickListener {
                itemClick!!.onClick(it,position)
            })
        }
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    class ViewHolder(binding: RvItemCustomersBookBinding) : RecyclerView.ViewHolder(binding.root),
        CompoundButton.OnCheckedChangeListener {
        var binding: RvItemCustomersBookBinding
        var item : ContactData?= null

        fun bind(contactData: ContactData) {
            this.item = contactData
            binding.firstChar.text = item!!.name!!.substring(0,1)
            binding.customerName.text = item!!.name

            if (item!!.phoneNumber.toString().contains("-")){
                binding.customerNumber.text = item!!.phoneNumber
            } else{
                binding.customerNumber.text = item!!.phoneNumber!!.substring(0,3) +
                        "-" + item!!.phoneNumber!!.substring(3,7) +
                        "-" + item!!.phoneNumber!!.substring(7)
            }

            binding.checkbox.isChecked = item!!.isChecked
        }
        init {
            this.binding = binding
            this.binding.checkbox.setOnCheckedChangeListener(this)
        }

        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            Log.d("check box", "check?")
        }

    }




}