package com.soccersim.presentation.ui.util

import android.graphics.Rect
import android.view.View
import androidx.annotation.Dimension
import androidx.recyclerview.widget.RecyclerView

class ItemMarginDecorator(
    private val offsets: Offsets
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            top = offsets.topDp.dpToPx
            left = offsets.leftDp.dpToPx
            right = offsets.rightDp.dpToPx
            bottom = offsets.bottomDp.dpToPx
        }
    }
}

data class Offsets(
    @Dimension(unit = Dimension.DP) val topDp: Int,
    @Dimension(unit = Dimension.DP) val bottomDp: Int,
    @Dimension(unit = Dimension.DP) val leftDp: Int,
    @Dimension(unit = Dimension.DP) val rightDp: Int
)