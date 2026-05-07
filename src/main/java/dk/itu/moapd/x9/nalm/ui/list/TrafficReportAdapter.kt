package dk.itu.moapd.x9.nalm.ui.list

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.squareup.picasso.Picasso
import dk.itu.moapd.x9.nalm.ui.list.TrafficReportModelLongClickListener
import dk.itu.moapd.x9.nalm.core.tag
import dk.itu.moapd.x9.nalm.databinding.ListItemTrafficReportBinding
import dk.itu.moapd.x9.nalm.domain.model.TrafficReportModel

class TrafficReportAdapter (
    private val longClickListener: TrafficReportModelLongClickListener,
    options: FirebaseRecyclerOptions<TrafficReportModel>,
) : FirebaseRecyclerAdapter<TrafficReportModel, TrafficReportAdapter.ViewHolder>(options) {



    class ViewHolder(private val binding: ListItemTrafficReportBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(trafficReport : TrafficReportModel) {
            binding.inputTrafficTitle.text = trafficReport.title
            binding.inputTrafficDate.text = trafficReport.date
            binding.inputTrafficSeverity.text = trafficReport.severity
            binding.inputTrafficReportType.text = trafficReport.reportType
            binding.inputTrafficDesc.text = trafficReport.desc
            binding.inputTrafficLocation.text = trafficReport.location

 
            Picasso.get().cancelRequest(binding.imageView)
            binding.imageView.setImageDrawable(null)
            // Load the new image if URL is available
            trafficReport.image?.let { url ->
                Picasso.get().load(url).into(binding.imageView)
            }
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