package dk.itu.moapd.x9.nalm

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import dk.itu.moapd.x9.nalm.databinding.ListItemTrafficReportBinding
import androidx.recyclerview.R
class CustomAdapter(
    private val items: List<TrafficReportModel>,
) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder =
        ListItemTrafficReportBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
            .let(::ViewHolder)


    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        Log.d("myTag", "Populate an item at position: $position")
// Bind the view holder with the selected `DummyModel` data.
        items[position].let(holder::bind)
    }
    override fun getItemCount() = items.size


    class ViewHolder(private val binding: ListItemTrafficReportBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(trafficReport: TrafficReportModel) {
            binding.trafficTitle.text = trafficReport.title
            binding.trafficLocation.text = trafficReport.location
            binding.trafficDate.text = trafficReport.date
            binding.trafficReportType.text = trafficReport.reportType
            binding.trafficSeverity.text = trafficReport.severity
            binding.trafficDesc.text = trafficReport.desc
        }
    }
}


