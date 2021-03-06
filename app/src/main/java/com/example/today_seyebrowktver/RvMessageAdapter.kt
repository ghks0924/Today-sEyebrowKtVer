package com.example.today_seyebrowktver

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.today_seyebrowktver.data.EachMessageData
import com.example.today_seyebrowktver.databinding.RvItemMessageBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RvMessageAdapter : RecyclerView.Adapter<RvMessageAdapter.ViewHolder> {

    var data: ArrayList<EachMessageData>? = null

    constructor(data: ArrayList<EachMessageData>) {
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvMessageAdapter.ViewHolder {
        val binding: RvItemMessageBinding =
            RvItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RvMessageAdapter.ViewHolder, position: Int) {
        holder.bind(data!![position])

        //itemClick event
        if (itemClick != null){
            holder?.binding.messageItemCardview.setOnClickListener(View.OnClickListener {
                itemClick?.onClick(it,position)
            })
        }

        //itemLongClick event
        if (itemLongClick != null  ) {
            holder?.binding.messageItemCardview.setOnLongClickListener(View.OnLongClickListener {
                itemLongClick?.onLongClick(it, position)
                return@OnLongClickListener true
            })
        }
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    class ViewHolder(binding: RvItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: RvItemMessageBinding

        fun bind(EachMessageData: EachMessageData) {
            // ??????????????? msec ?????? ?????????.
            val now = System.currentTimeMillis()
            // ??????????????? date ????????? ????????????.
            val date = Date(now)
            // ????????? ????????? ????????? ????????? ( yyyy/MM/dd ?????? ????????? ?????? ?????? )
            val sdfNow = SimpleDateFormat("yyyyMMddhhmmss")
            val messageDate = sdfNow.format(date)


            binding.messageTitle.text = EachMessageData.messageTitle
            binding.messageContent.text = EachMessageData.messageContent

        }

        init {
            this.binding = binding
        }
    }

}