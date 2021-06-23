package com.example.today_seyebrowktver

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PaintFlagsDrawFilter
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.today_seyebrowktver.databinding.RvItemMessageEditGroupsBinding
import com.example.today_seyebrowktver.databinding.RvItemMessageGroupsBinding
import java.util.*
import kotlin.collections.ArrayList

class RvMessageGroupEditAdapter : RecyclerView.Adapter<RvMessageGroupEditAdapter.ViewHolder> {

    var data: ArrayList<MessageGroupData>? = null

    constructor(data: ArrayList<MessageGroupData>) {
        this.data = data
    }

    interface PlusIconClick {
        fun onPlusClick(view: View, position: Int)
    }

    interface DeleteIconClick {
        fun onClick(view: View, position: Int)
    }

    interface ItemLongClick {
        fun onLongClick(view: View, position: Int)
    }

    var deleteClick: DeleteIconClick? = null
    var plusClick: PlusIconClick? = null
    var itemLongClick: ItemLongClick? = null

    var isDragUsed = false


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RvMessageGroupEditAdapter.ViewHolder {
        val binding: RvItemMessageEditGroupsBinding =
            RvItemMessageEditGroupsBinding.inflate(LayoutInflater.from(parent.context),
                parent,
                false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RvMessageGroupEditAdapter.ViewHolder, position: Int) {
        holder.bind(data!![position])

        //itemClick event
        if (deleteClick != null) {
            holder?.binding.deleteIcon.setOnClickListener(View.OnClickListener {
                deleteClick?.onClick(it, position)
            })
        }

        if (plusClick != null) {
            holder?.binding.plusIcon.setOnClickListener(View.OnClickListener {
                plusClick?.onPlusClick(it, position)
            })
        }

        //itemLongClick event
        if (itemLongClick != null) {
            holder?.binding.fixedLayout.setOnLongClickListener(View.OnLongClickListener {
                itemLongClick?.onLongClick(it, position)
                return@OnLongClickListener true
            })
        }

    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    class ViewHolder(binding: RvItemMessageEditGroupsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var binding: RvItemMessageEditGroupsBinding

        fun bind(messageGroup: MessageGroupData) {

            //삭제 체크가 된 애들은 변경해서 출력
            if (messageGroup.deleteCheck) {
                binding.messageOrderTv.text = " -  "
                binding.messageOrderTv.setTextColor(Color.parseColor("#dfdfdf"))

                binding.messageGroupTv.text = messageGroup.groupName
                binding.messageGroupTv.setTextColor(Color.parseColor("#dfdfdf"))

                binding.deleteIcon.visibility = View.GONE
                binding.plusIcon.visibility = View.VISIBLE
                binding.menuIcon.imageTintList = ColorStateList.valueOf(Color.parseColor("#dfdfdf"))

            } else {  //삭제 체크가 안 된 애들은 정상적으로 출력

                binding.messageOrderTv.text = (messageGroup.order + 1).toString() + "  "
                binding.messageOrderTv.setTextColor(Color.parseColor("#0c0a4d"))

                binding.messageGroupTv.text = messageGroup.groupName
                binding.messageGroupTv.setTextColor(Color.parseColor("#4f4f4f"))

                binding.plusIcon.visibility = View.GONE
                binding.deleteIcon.visibility = View.VISIBLE

                binding.menuIcon.imageTintList = ColorStateList.valueOf(Color.parseColor("#4D4f4f4f"))
                binding.deleteIcon.imageTintList = ColorStateList.valueOf(Color.parseColor("#4D4f4f4f"))
            }
        }

        init {
            this.binding = binding
        }
    }

    //arrayList 는 어뎁터에서 사용하고 있는 List, 순서를 변경하는 함수
    fun onItemDragMove(beforePosition : Int, afterPosition : Int){
        isDragUsed = true
        if(beforePosition < afterPosition){
            for (i in beforePosition until afterPosition) {
                Collections.swap(data, i, i + 1)
            }
        } else {
            for (i in beforePosition downTo afterPosition + 1) {
                Collections.swap(data, i, i - 1)
            }
        }

        notifyItemMoved(beforePosition, afterPosition)
    }

    //드래그앤 드롭 터치를 마무리 하였을 경우 들어오는 함수
    fun changeMoveEvent(){
        //순서 정리
        outer@while (true){
            for (i in 0 until data!!.size-1){
                if (!data!![i].deleteCheck){ //삭제 클릭된 애가 아니면 그냥 넘김

                } else if (data!![i].deleteCheck && !data!![i+1].deleteCheck) { //이 다음거랑 비교해서 순서바꿈0
                    Collections.swap(data, i, i+1)
                    continue@outer
                } else if (data!![i].deleteCheck && data!![i+1].deleteCheck){

                }
            }
            break
        }

        //다 정리가 되면 새롭게 순서정의
        for (i in 0 until data!!.size){
            data!![i].order = i
        }

        notifyDataSetChanged()
    }


}