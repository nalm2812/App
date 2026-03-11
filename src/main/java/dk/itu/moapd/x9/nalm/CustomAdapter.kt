package dk.itu.moapd.x9.nalm

import android.R.attr.data
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class CustomAdapter(
    private val items: List<TrafficReportModel>,
) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder =
        RowItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
            .let(::ViewHolder)
    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        Log.d("myTag", "Populate an item at position: $position")
// Bind the view holder with the selected `DummyModel` data.
        data[position].let(holder::bind)
    }
    override fun getItemCount() = data.size
}
