package dk.itu.moapd.x9.nalm.ui.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.database.childEvents
import com.google.firebase.database.database
import dk.itu.moapd.x9.nalm.core.DATABASE_URL
import dk.itu.moapd.x9.nalm.R
import dk.itu.moapd.x9.nalm.core.API_KEY
import dk.itu.moapd.x9.nalm.ui.list.SwipeToDeleteCallback
import dk.itu.moapd.x9.nalm.ui.list.TrafficReportAdapter
import dk.itu.moapd.x9.nalm.domain.model.TrafficReportModel
import dk.itu.moapd.x9.nalm.ui.list.TrafficReportModelLongClickListener
import dk.itu.moapd.x9.nalm.data.repository.TrafficReportRepository
import dk.itu.moapd.x9.nalm.ui.dialogs.UpdateDataDialogFragment
import dk.itu.moapd.x9.nalm.core.preferences.LocationTrackingPreferences
import dk.itu.moapd.x9.nalm.databinding.FragmentDashboardBinding
import dk.itu.moapd.x9.nalm.service.LocationService
import dk.itu.moapd.x9.nalm.core.tag
import dk.itu.moapd.x9.nalm.ui.utils.viewBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject


/**
 * A simple [androidx.fragment.app.Fragment] subclass.
 * Use the [DashboardFragment] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private val binding by viewBinding(FragmentDashboardBinding::bind)
    private val viewModel: MainViewModel by activityViewModels()

    private val repository by lazy { TrafficReportRepository() }

    private var adapter: TrafficReportAdapter? = null


    private var latitude: String? = null
    private var longitude: String? = null
    private var pendingStartTracking: Boolean = false

    private var locationServiceBound: Boolean = false

    private var locationService: LocationService? = null


    private fun updateLocationDetails(location: Location) {
        Log.v("testing", "does this run?????")
        latitude = location.latitude.toString()
        longitude = location.longitude.toString()
        viewModel.setLatitude(location.latitude)
        viewModel.setLongitude(location.longitude)

    }







    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        Log.v("myTag", "onCreateView Dashboard was called")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        val userId = repository.currentUserId() ?: return

        val query = Firebase.database(DATABASE_URL).reference
            .child("trafficreport")
            .child(userId)
            .orderByChild("createdAt")



        val options = FirebaseRecyclerOptions.Builder<TrafficReportModel>()
            .setQuery(query, TrafficReportModel::class.java)
            .setLifecycleOwner(viewLifecycleOwner)
            .build()

        adapter = TrafficReportAdapter(
            longClickListener = TrafficReportModelLongClickListener { trafficReport, position ->

                val key =
                    adapter?.getRef(position)?.key ?: return@TrafficReportModelLongClickListener
                UpdateDataDialogFragment
                    .createInstance(
                        key = key,
                        currentName = trafficReport.title,
                        createdAt = trafficReport.createdAt,
                        currentSeverity = trafficReport.severity,
                        currentReportType = trafficReport.reportType,
                        currentDate = trafficReport.date,
                        currentDesc = trafficReport.desc,
                        latitude = trafficReport.latitude,
                        longitude = trafficReport.longitude,
                        image = trafficReport.image,
                        landscape = trafficReport.landscape
                    )
                    .apply { isCancelable = false }
                    .show(parentFragmentManager, tag())
            },
            options = options,
        )

        setupRecyclerView(requireNotNull(adapter))




        Log.v("myTag", "onViewCreated Dashboard was called")
    }






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("myTag", "onCreate Dashboard was called")
    }

    override fun onStart() {
        super.onStart()


        Log.v("myTag", "onStart Dashboard was called")


    }


    override fun onStop() {
        super.onStop()
        Log.v("testing", "stop")

        viewModel.setLatitude(null)
        viewModel.setLongitude(null)
        latitude = null
        longitude = null

        Log.v("myTag", "onStop Dashboard was called")

    }


    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null

        Log.v("myTag", "onDestroyView Dashboard was called")

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.v("myTag", "onViewStateRestored Dashboard was called")

    }

    override fun onResume() {
        super.onResume()

        Log.v("myTag", "onResume Dashboard was called")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.v("myTag", "onSaveInstanceState Dashboard was called")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v("testing", "stop")

        Log.v("myTag", "onDestroy Dashboard was called")

    }

    private fun setupRecyclerView(adapter: TrafficReportAdapter) =
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = null
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            this.adapter = adapter
            val swipeHandler = object : SwipeToDeleteCallback() {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    super.onSwiped(viewHolder, direction)
                    val userId = repository.currentUserId() ?: return
                    val pos = viewHolder.bindingAdapterPosition
                    val key = adapter.getRef(pos).key ?: return
                    repository.deleteTrafficReport(userId = userId, key = key)
                }
            }

            ItemTouchHelper(swipeHandler).attachToRecyclerView(this)

        }



    /*private fun getAndUpdateAirQuality(){

        val url = "https://airquality.googleapis.com/v1/currentConditions:lookup?key=${API_KEY}"
        val requestQueue = Volley.newRequestQueue(context)

        val postdata2 = JSONObject()
        postdata2.put("latitude", latitude)
        postdata2.put("longitude", longitude)
        val postdata = JSONObject()
        postdata.put("location", postdata2)



            val stringRequest = object : JsonObjectRequest(Method.POST, url,
                postdata, Response.Listener { response ->
                    try {
                        val indexes = response.getJSONArray("indexes")
                        val item = indexes.getJSONObject(0)
                        val aqi = item.getString("aqi")
                        val category = item.getString("category")
                        //binding.header.inputAirQualityIndex.text = aqi
                        //binding.header.inputAirQualityCategory.text = category
                    } catch (e: Exception) {
                        Log.v("testing", "ERROR ERROR 2")
                        e.printStackTrace()
                    }

                },
                Response.ErrorListener { error ->
                    Log.v("testing", "ERROR ERROR 1")
                    error.printStackTrace()
                }) {

            }

            stringRequest.retryPolicy = DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )

            requestQueue.add(stringRequest)


    }*/
    //code from https://stackoverflow.com/questions/60672406/how-to-use-coroutine-in-kotlin-to-call-a-function-every-second
    /*val scope = MainScope()
    var job: Job? = null

    fun startUpdates() {
        Log.v("testing", "start")
        stopUpdates()
        job = scope.launch {
            while(true) {
                if (latitude!=null && longitude!=null) {
                    getAndUpdateAirQuality()
                }
                delay(1000)
            }
        }
    }

    fun stopUpdates() {
        if (job!=null){
            job?.cancel()
            job = null
        }

    }*/






}


