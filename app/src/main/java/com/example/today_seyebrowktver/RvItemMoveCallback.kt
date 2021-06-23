package com.example.today_seyebrowktver

import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class RvItemMoveCallback constructor(val adapter: RvMessageGroupEditAdapter) : ItemTouchHelper.Callback() {
    private var isMoved = false    //무빙 이벤트에 대한 boolean값

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val flagDrag = ItemTouchHelper.UP or ItemTouchHelper.DOWN    //드래그앤 드롭 움직임 설정
        return makeMovementFlags(flagDrag, 0)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        adapter.onItemDragMove(viewHolder.bindingAdapterPosition, target.bindingAdapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    override fun isLongPressDragEnabled(): Boolean {

        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }


    //drag시 색상 변화
    override fun onChildDrawOver(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder?,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean,
    ) {
        super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        if (actionState === ItemTouchHelper.ACTION_STATE_DRAG) {
            val itemView: View = viewHolder!!.itemView
            c.save()
            c.clipRect(itemView.getLeft() + dX,
                itemView.getTop() + dY,
                itemView.getRight() + dX,
                itemView.getBottom() + dY)
            c.translate(itemView.getLeft() + dX, itemView.getTop() + dY)

            // draw the frame
            c.drawColor(Color.parseColor("#4D0c0a4d"))
            c.restore()
        }
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if(isMoved){
            isMoved = false
            adapter.changeMoveEvent()
        }
    }

    override fun onMoved(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        fromPos: Int,
        target: RecyclerView.ViewHolder,
        toPos: Int,
        x: Int,
        y: Int,
    ) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
        isMoved = true
    }


}