package dk.itu.moapd.x9.nalm

import androidx.recyclerview.widget.RecyclerView

class ViewHolder(private val binding: RowItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(trafficReport: TrafficReportModel) {
        binding.textViewTitle.text = trafficReport.cityName
        ...
    }
}
