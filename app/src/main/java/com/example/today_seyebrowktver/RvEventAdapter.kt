package com.example.today_seyebrowktver

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.today_seyebrowktver.data.EventData
import com.example.today_seyebrowktver.databinding.RvItemEventBinding

class RvEventAdapter : RecyclerView.Adapter<RvEventAdapter.ViewHolder> {

    var data: List<EventData>? = null

    constructor(data: List<EventData>) {
        this.data = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvEventAdapter.ViewHolder {
        val binding: RvItemEventBinding =
            RvItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RvEventAdapter.ViewHolder, position: Int) {
        holder.bind(data!![position])
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    inner class ViewHolder(binding: RvItemEventBinding) : RecyclerView.ViewHolder(binding.root){
        var binding :RvItemEventBinding

        fun bind(eventData: EventData){
            binding.dateTv.text = eventData.date.toString()
            binding.nameTv.text = eventData.customerName
        }

        init {
            this.binding = binding
        }
    }

}