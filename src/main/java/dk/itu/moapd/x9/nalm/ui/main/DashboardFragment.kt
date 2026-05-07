package dk.itu.moapd.x9.nalm.ui.main

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
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
import com.android.volley.Request.Method
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.database.database
import dk.itu.moapd.x9.nalm.core.DATABASE_URL
import dk.itu.moapd.x9.nalm.ui.main.MainViewModel
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.json.JSONObject


/**
 * A simple [androidx.fragment.app.Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment(R.layout.fragment_dashboard), SharedPreferences.OnSharedPreferenceChangeListener {

    private val binding by viewBinding(FragmentDashboardBinding::bind)
    private val viewModel: MainViewModel by activityViewModels()

    private val repository by lazy { TrafficReportRepository() }

    private var adapter: TrafficReportAdapter? = null


    private var latitude: String? = null
    private var longitude: String? = null
    private var pendingStartTracking: Boolean = false

    private var locationServiceBound: Boolean = false

    private var locationService: LocationService? = null

    private val sharedPreferences: SharedPreferences by lazy {
        requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE,
        )
    }





    private val serviceConnection = object : ServiceConnection {

        /**
         * Called when a connection to the Service has been established, with the
         * `android.os.IBinder` of the communication channel to the Service.
         *
         * If the system has started to bind your client app to a service, it's possible that your
         * app will never receive this callback. Your app won't receive a callback if there's an
         * issue with the service, such as the service crashing while being created.
         *
         * @param name The concrete component name of the service that has been connected.
         * @param service The IBinder of the Service's communication channel, which you can now make
         *      calls on.
         */
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as LocationService.LocalBinder
            locationService = binder.service
            locationServiceBound = true

            if (pendingStartTracking) {
                locationService?.subscribeToLocationUpdates()
                pendingStartTracking = false
            }

            locationService?.let { svc ->
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        svc.locationUpdates.collect(::updateLocationDetails)

                    }
                }
            }
        }

        /**
         * Called when a connection to the Service has been lost. This typically happens when the
         * process hosting the service has crashed or been killed. This does not remove the
         * ServiceConnection itself -- this binding to the service will remain active, and you will
         * receive a call to `onServiceConnected()` when the Service is next running.
         *
         * @param name The concrete component name of the service whose connection has been lost.
         */
        override fun onServiceDisconnected(name: ComponentName) {
            locationService = null
            locationServiceBound = false
        }
    }
    private fun updateLocationDetails(location: Location) {
        latitude = location.latitude.toString()
        longitude = location.longitude.toString()
        getAirQuality()
    }







    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = super.onCreateView(inflater, container, savedInstanceState)

        Log.v("myTag", "onCreateView Dashboard was called")
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (LocationTrackingPreferences.isTrackingEnabled(requireContext())) {
            locationService?.unsubscribeToLocationUpdates()
            pendingStartTracking = false
            requireActivity().stopService(
                Intent(
                    requireContext(), LocationService::class.java
                )
            )
        } else {
            if (hasLocationPermission()) {
                startLocationTracking()
            } else {
                requestLocationPermission()
            }
        }

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
                UpdateDataDialogFragment.Companion
                    .createInstance(
                        key = key,
                        currentName = trafficReport.title,
                        createdAt = trafficReport.createdAt,
                        currentSeverity = trafficReport.severity,
                        currentReportType = trafficReport.reportType,
                        currentLocation = trafficReport.location,
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
        //val adapter = TrafficReportAdapter(this, options)

        setupRecyclerView(requireNotNull(adapter))




        //setUpList()
        Log.v("myTag", "onViewCreated Dashboard was called")
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startLocationTracking()
        } else {
            Snackbar.make(
                binding.root,
                R.string.permission_denied_message,
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun startLocationTracking() {
        pendingStartTracking = true
        val serviceIntent = Intent(requireContext(), LocationService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(requireActivity(), serviceIntent)
        } else {
            requireActivity().startService(serviceIntent)
        }
        if (locationServiceBound) {
            locationService?.subscribeToLocationUpdates()
            pendingStartTracking = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("myTag", "onCreate Dashboard was called")
    }

    override fun onStart() {
        super.onStart()
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)

        val serviceIntent = Intent(requireContext(), LocationService::class.java)
        requireActivity().bindService(
            serviceIntent,
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
        val alreadyEnabled = LocationTrackingPreferences.isTrackingEnabled(requireContext())
        if (alreadyEnabled) {
            startLocationTracking()
        }
        Log.v("myTag", "onStart Dashboard was called")

    }


    override fun onStop() {
        super.onStop()
        if (locationServiceBound) {
            requireActivity().unbindService(serviceConnection)
            locationServiceBound = false
        }

        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        viewModel.setLatitude(null)
        viewModel.setLongitude(null)
        Log.v("myTag", "onStop Dashboard was called")

    }
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        if (key == LocationTrackingPreferences.KEY_TRACKING_ENABLED) {
            LocationTrackingPreferences.isTrackingEnabled(requireContext())

        }
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
            //Log.v("myTag", "size of traffic: " + trafficReportList.size)
            /*viewModel.items.observe(viewLifecycleOwner, Observer { list ->
                trafficReportList = list
                adapter = CustomAdapter(trafficReportList)

                ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
                    val navBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
                    view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                        bottomMargin = navBarHeight
                    }
                    insets
                }
            })*/
            /*val swipeHandler = object : SwipeToDeleteCallback() {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    super.onSwiped(viewHolder, direction)
                    val userId = repository.currentUserId() ?: return
                    val pos = viewHolder.bindingAdapterPosition
                    val key = adapter.getRef(pos).key ?: return
                    repository.deleteTrafficReport(userId = userId, key = key)
                }
            }

            ItemTouchHelper(swipeHandler).attachToRecyclerView(this)*/
        }


    /*private fun setUpList() {

        binding.composeView.apply{
            setContent {
                X9Theme {
                    ListScreen()
                }
            }
        }
    }
    @Composable
    fun ListScreen() {
        val data by viewModel.items.observeAsState(emptyList())
        Scaffold(
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)

            ) {
                items(data) { item ->
                    TrafficReportItem(item)
                }
            }
        }
    }*/

    private fun getAirQuality(){
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
                    Log.v("testing", aqi)
                } catch (e: Exception) {
                    Log.v("testing", "ERROR ERROR 2")
                }

            },
            Response.ErrorListener { error ->
                Log.v("testing", "ERROR ERROR 1")
                error.printStackTrace()
            }) {

        }

        requestQueue.add(stringRequest)
    }






}


