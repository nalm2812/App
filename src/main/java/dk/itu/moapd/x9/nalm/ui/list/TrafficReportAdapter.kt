package dk.itu.moapd.x9.nalm.ui.list

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.squareup.picasso.Picasso
import dk.itu.moapd.x9.nalm.core.BUCKET_URL
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

            Picasso.get().cancelRequest(binding.imageView)
            binding.imageView.setImageDrawable(null)
            if (trafficReport.image!=null){
                Firebase.storage(BUCKET_URL).reference
                    .child("images/${trafficReport.image}").downloadUrl
                    .addOnSuccessListener { url ->
                        if (trafficReport.landscape == true){
                            Picasso.get().load(url).into(binding.imageView)

                        }else{
                            Picasso.get().load(url).rotate(90f).into(binding.imageView)

                        } }

            }
            // Load the new image if URL is available
            /*Log.v("testing3", "idk here???")
            trafficReport.image?.let { url ->
                Log.v("testing3", url)
                if (trafficReport.landscape == true){
                    Picasso.get().load(url).into(binding.imageView)

                }else{
                    Picasso.get().load(url).rotate(90f).into(binding.imageView)

                }
            }*/
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