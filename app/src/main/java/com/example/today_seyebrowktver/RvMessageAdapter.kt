package com.example.today_seyebrowktver

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.today_seyebrowktver.databinding.RvItemMessageBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RvMessageAdapter : RecyclerView.Adapter<RvMessageAdapter.ViewHolder> {

    var data: ArrayList<MessageData>? = null

    constructor(data: ArrayList<MessageData>) {
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

        fun bind(messageData: MessageData) {
            // 현재시간을 msec 으로 구한다.
            val now = System.currentTimeMillis()
            // 현재시간을 date 변수에 저장한다.
            val date = Date(now)
            // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
            val sdfNow = SimpleDateFormat("yyyyMMddhhmmss")
            val messageDate = sdfNow.format(date)


            binding.messageTypeTv.text = messageData.messageType
            binding.messageTitle.text = messageData.messageTitle
            binding.messageDate.text = messageDate.toString().trim()
            binding.messageContent.text = messageData.messageContent

        }

        init {
            this.binding = binding
        }
    }

}