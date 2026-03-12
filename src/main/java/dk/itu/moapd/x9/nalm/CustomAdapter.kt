package dk.itu.moapd.x9.nalm

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import dk.itu.moapd.x9.nalm.databinding.ListItemTrafficReportBinding
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
        items[position].let(holder::bind)
    }
    override fun getItemCount() = items.size


    class ViewHolder(private val binding: ListItemTrafficReportBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(trafficReport: TrafficReportModel) {
            binding.inputTrafficTitle.text = trafficReport.title
            binding.inputTrafficLocation.text = trafficReport.location
            binding.inputTrafficDate.text = trafficReport.date
            binding.inputTrafficReportType.text = trafficReport.reportType
            binding.inputTrafficSeverity.text = trafficReport.severity
            binding.inputTrafficDesc.text = trafficReport.desc
        }
    }
}


