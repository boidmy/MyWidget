package util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import util.Util.dpToPx


class ItemDecoration(private val size: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = size.dpToPx
    }
}