package dk.itu.moapd.x9.nalm.ui.list

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import dk.itu.moapd.x9.nalm.R
import dk.itu.moapd.x9.nalm.ui.common.showSnackBar

open class SwipeToDeleteCallback : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
) {
    /**
     * Called when `ItemTouchHelper` wants to move the dragged item from its old position to the new
     * position.
     *
     * If this method returns true, `ItemTouchHelper` assumes `viewHolder` has been moved to the
     * adapter position of target `ViewHolder`.
     *
     * If you don't support drag & drop, this method will never be called.
     *
     * @param recyclerView The `RecyclerView` to which `ItemTouchHelper` is attached to.
     * @param viewHolder The `ViewHolder` which is being dragged by the user.
     * @param target The `ViewHolder` over which the currently active item is being dragged.
     *
     * @return `True` if the `viewHolder` has been moved to the adapter position of target.
     */
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean = false

    /**
     * Called when a `ViewHolder` is swiped by the user.
     *
     * If you are returning relative directions `START`, `END` from the
     * `getMovementFlags(RecyclerView, ViewHolder)` method, this method will also use relative
     * directions. Otherwise, it will use absolute directions.
     *
     * If you don't support swiping, this method will never be called.
     *
     * `ItemTouchHelper` will keep a reference to the `View` until it is detached from
     * `RecyclerView`. As soon as it is detached, `ItemTouchHelper` will call
     * `clearView(RecyclerView, ViewHolder)`.
     *
     * @param viewHolder The `ViewHolder` which has been swiped by the user.
     * @param direction The direction to which the `ViewHolder` is swiped. It is one of `UP`,
     *      `DOWN`, `LEFT` or `RIGHT`. If your `getMovementFlags(RecyclerView, ViewHolder)` method
     *      returned relative flags instead of `LEFT` / `RIGHT`; `direction` will be relative as
     *      well.
     */
    override fun onSwiped(
        viewHolder: RecyclerView.ViewHolder,
        direction: Int
    ) {
        viewHolder.itemView.showSnackBar(
            ContextCompat.getString(viewHolder.itemView.context, R.string.item_deleted)
        )
    }
}