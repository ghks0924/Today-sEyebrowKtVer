package com.example.today_seyebrowktver

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class RecyclerDecorationHeight: RecyclerView.ItemDecoration {

    private var divHeight: Int? = null

    constructor (divHeight: Int) {
        this.divHeight = divHeight
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.getChildAdapterPosition(view) != parent.adapter!!.itemCount - 1) {
            outRect.bottom = divHeight!!
        }
    }
}