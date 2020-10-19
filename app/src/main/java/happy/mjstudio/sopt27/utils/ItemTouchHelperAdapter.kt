package happy.mjstudio.sopt27.utils

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean

    fun onItemDismiss(position: Int)
}

class SimpleItemTouchHelperCallback(private val adapter: ItemTouchHelperAdapter) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END, ItemTouchHelper.START
) {
    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun onMove(
        recyclerView: RecyclerView, viewHolder: ViewHolder, target: ViewHolder
    ): Boolean {
        val pos1 = viewHolder.adapterPosition
        val pos2 = target.adapterPosition

        return adapter.onItemMove(pos1, pos2)
    }

    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
        val pos = viewHolder.adapterPosition

        adapter.onItemDismiss(pos)
    }
}

