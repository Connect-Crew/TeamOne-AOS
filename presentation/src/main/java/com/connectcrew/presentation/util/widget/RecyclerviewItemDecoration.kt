package com.connectcrew.presentation.util.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.connectcrew.presentation.util.toPx

class RecyclerviewItemDecoration(
    private val top: Int,
    private val bottom: Int,
    private val start: Int,
    private val end: Int,
    private vararg val viewTypes: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        setVerticalOrientationDecoration(outRect, view, parent)
    }

    private fun setVerticalOrientationDecoration(outRect: Rect, view: View, parent: RecyclerView) {
        val context = parent.context

        val viewHolder = parent.getChildViewHolder(view)

        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) {
            return
        }

        parent.adapter?.getItemViewType(position)?.let {
            if (viewTypes.contains(it)) {
                outRect.left = this.start.toPx(context)
                outRect.right = this.end.toPx(context)

                val isFirstItem = viewHolder.absoluteAdapterPosition == 0
                val isLastItem = parent.adapter?.itemCount?.minus(1) == viewHolder.absoluteAdapterPosition

                when {
                    isFirstItem && isLastItem -> return@let
                    isFirstItem -> outRect.bottom = this.bottom.toPx(context)
                    else -> {
                        outRect.top = this.top.toPx(context)
                        outRect.bottom = this.bottom.toPx(context)
                    }
                }
            }
        }
    }
}