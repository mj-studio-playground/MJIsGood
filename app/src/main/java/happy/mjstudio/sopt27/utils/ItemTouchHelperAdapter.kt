package happy.mjstudio.sopt27.utils

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int)
}

class SimpleItemTouchHelperCallback(private val adapter: ItemTouchHelperAdapter) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END, ItemTouchHelper.START
) {
    private var swipeBack = false

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }

    override fun onMove(
        recyclerView: RecyclerView, viewHolder: ViewHolder, target: ViewHolder
    ): Boolean {
        val pos1 = viewHolder.adapterPosition
        val pos2 = target.adapterPosition

        adapter.onItemMove(pos1, pos2)
        return true
    }

    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {

    }

    fun attachToRecyclerView(rv: RecyclerView) {
        ItemTouchHelper(this).attachToRecyclerView(rv)
    }
}

