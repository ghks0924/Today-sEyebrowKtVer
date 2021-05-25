package com.example.today_seyebrowktver

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.today_seyebrowktver.databinding.RvItemMessageGroupsBinding
import kotlin.collections.ArrayList

class RvMessageGroupAdapter : RecyclerView.Adapter<RvMessageGroupAdapter.ViewHolder> {

    var data: ArrayList<MessageGroupData>? = null

    constructor(data: ArrayList<MessageGroupData>) {
        this.data = data
    }

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    interface ItemLongClick {
        fun onLongClick(view: View, position: Int)
    }


    var itemClick: ItemClick? = null
    var itemLongClick: ItemLongClick? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvMessageGroupAdapter.ViewHolder {
        val binding: RvItemMessageGroupsBinding =
            RvItemMessageGroupsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RvMessageGroupAdapter.ViewHolder, position: Int) {
        holder.bind(data!![position])

        //itemClick event
        if (itemClick != null){
            holder?.binding.fixedLayout.setOnClickListener(View.OnClickListener {
                itemClick?.onClick(it,position)
            })
        }

        //itemLongClick event
        if (itemLongClick != null  ) {
            holder?.binding.fixedLayout.setOnLongClickListener(View.OnLongClickListener {
                itemLongClick?.onLongClick(it, position)
                return@OnLongClickListener true
            })
        }

    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    class ViewHolder(binding: RvItemMessageGroupsBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: RvItemMessageGroupsBinding

        fun bind(messageGroup: MessageGroupData) {

            binding.messageGroupTv.text = messageGroup.groupName

        }

        init {
            this.binding = binding
        }
    }

}