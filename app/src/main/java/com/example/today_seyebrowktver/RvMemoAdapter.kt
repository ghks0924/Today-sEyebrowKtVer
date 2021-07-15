package com.example.today_seyebrowktver

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.today_seyebrowktver.data.MemoData
import com.example.today_seyebrowktver.databinding.RvItemMemoBinding
import java.text.SimpleDateFormat
import java.util.*

class RvMemoAdapter: RecyclerView.Adapter<RvMemoAdapter.ViewHolder> {

    interface ItemClick{
        fun onClick(view: View, position: Int)
    }

    interface ItemLongClick{
        fun onLongClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null
    var itemLongClick: ItemLongClick? = null

    var data: List<MemoData>? = null

    constructor(data: List<MemoData>){
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

        //itemClick.onClick 호출
        if (itemClick != null){
            holder?.binding.memoItemCardview.setOnClickListener(View.OnClickListener {
                itemClick?.onClick(it, position)
            })
        }

        //itemLongClick.onLongClick 호출
        if (itemLongClick != null){
            holder?.binding.memoItemCardview.setOnLongClickListener(View.OnLongClickListener {
                itemLongClick?.onLongClick(it, position)
                return@OnLongClickListener true
            })
        }
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    class ViewHolder(binding: RvItemMemoBinding) : RecyclerView.ViewHolder(binding.root){
        var binding: RvItemMemoBinding

        fun bind(memoData: MemoData){
            if (memoData.memoTitle.isNullOrEmpty()){ //제목이 굳이 비워있으면 영역 비우기
                binding.memoTitle.visibility = View.GONE
            } else{
                    binding.memoTitle.text = memoData.memoTitle

            }

            if (memoData.memoContent.isNullOrEmpty()){  //메모 내용이 굳이 비워있으면 영역 비우기
                binding.memoContent.visibility = View.GONE
            } else{
                binding.memoContent.text = memoData.memoContent
            }



//             현재시간을 msec 으로 구한다.

            // 현재시간을 msec 으로 구한다.
            val now = System.currentTimeMillis()
            // 현재시간을 date 변수에 저장한다.
            val date = Date(now)
            // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
            val sdfNow = SimpleDateFormat("yyyyMMddhhmmss")

            // nowDate 변수에 값을 저장한다.
            val nowYear = sdfNow.format(date).substring(0, 4)
            val nowMonthDay = sdfNow.format(date).substring(4, 8)

            //메모의 데이터 구하기
            val tmpDate: String = memoData.memoDate
            val memoYear = tmpDate.substring(0, 4)
            val memoMonthDay = tmpDate.substring(4, 8)

            var dspDateStr: String? = null
            if (nowYear == memoYear) { //현재 년도와 메모의 저장된 년도가 같으면
                if (nowMonthDay == memoMonthDay) { //월,일도 같으면
                    dspDateStr = tmpDate.substring(8, 10) + "시 " + tmpDate.substring(10, 12) + "분"
                } else { //다르면
                    dspDateStr = tmpDate.substring(4, 6) + "월 "+tmpDate.substring(6, 8) + "일"
                }
            } else { //다르면
                dspDateStr = tmpDate.substring(0, 4) + "년 " + tmpDate.substring(4, 6) + "월 "+tmpDate.substring(6, 8) + "일"
            }

            binding.memoDate.text = dspDateStr
            binding.memoContent.text = memoData.memoContent

        }

        init {
            this.binding = binding

        }
    }

}