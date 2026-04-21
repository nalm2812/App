package dk.itu.moapd.x9.nalm

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import dk.itu.moapd.x9.nalm.databinding.ListItemTrafficReportBinding


class TrafficReportAdapter (
    private val longClickListener: TrafficReportModelLongClickListener,
    options: FirebaseRecyclerOptions<TrafficReportModel>,
) : FirebaseRecyclerAdapter<TrafficReportModel, TrafficReportAdapter.ViewHolder>(options) {
    class ViewHolder(private val binding: ListItemTrafficReportBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(trafficReport : TrafficReportModel) {
            binding.trafficTitle.text = trafficReport.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ListItemTrafficReportBinding
        .inflate(LayoutInflater.from(parent.context), parent, false)
        .let(::ViewHolder)

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: TrafficReportModel) {
        Log.d(tag(), "Bind item at position=$position")
        holder.bind(model)

        holder.itemView.setOnLongClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                longClickListener.onTrafficReportLongClick(model, pos)
            }
            true
        }
    }


    }