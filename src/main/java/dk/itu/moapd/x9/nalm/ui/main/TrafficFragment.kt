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
import android.view.KeyEvent
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
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.x9.nalm.DropdownMenu
import dk.itu.moapd.x9.nalm.R
import dk.itu.moapd.x9.nalm.core.preferences.LocationTrackingPreferences
import dk.itu.moapd.x9.nalm.data.repository.TrafficReportRepository
import dk.itu.moapd.x9.nalm.databinding.FragmentTrafficBinding
import dk.itu.moapd.x9.nalm.domain.model.TrafficReportModel
import dk.itu.moapd.x9.nalm.service.LocationService
import dk.itu.moapd.x9.nalm.ui.common.showToast
import dk.itu.moapd.x9.nalm.ui.theme.X9Theme
import dk.itu.moapd.x9.nalm.ui.utils.viewBinding
import kotlinx.coroutines.launch

class TrafficFragment : Fragment(R.layout.fragment_traffic), SharedPreferences.OnSharedPreferenceChangeListener {
    private val binding by viewBinding(FragmentTrafficBinding::bind)
    private val viewModel: MainViewModel by activityViewModels()


    private val repository by lazy { TrafficReportRepository() }

    private val sharedPreferences: SharedPreferences by lazy {
        requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE,
        )
    }








    private var longitude: Double? = null
    private var latitude: Double? = null



    private var pendingStartTracking: Boolean = false

    private var locationServiceBound: Boolean = false

    private var locationService: LocationService? = null





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
           latitude = location.latitude
            longitude = location.longitude

    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v("myTag", "onCreateView Traffic was called")
        return super.onCreateView(inflater, container, savedInstanceState)

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
        setupUI()
        //setSeverity(view)
        //setReportType(view)
        setUpReportType()
        setUpSeverity()
        Log.v("myTag", "onViewCreated Traffic was called")

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




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.v("myTag", "onCreate Traffic was called")
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
        Log.v("myTag", "onStart Traffic was called")
    }
    private fun startLocationTracking() {
        pendingStartTracking = true
        val serviceIntent = Intent(requireContext(), LocationService::class.java)
        ContextCompat.startForegroundService(requireActivity(), serviceIntent)
        if (locationServiceBound) {
            locationService?.subscribeToLocationUpdates()
            pendingStartTracking = false
        }
    }

    override fun onStop() {
        super.onStop()
        if (locationServiceBound) {
            requireActivity().unbindService(serviceConnection)
            locationServiceBound = false
        }

        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        Log.v("myTag", "onStop Traffic was called")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.v("myTag", "onDestroyView Traffic was called")

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.v("myTag", "onViewStateRestored Traffic was called")

    }

    override fun onResume() {
        super.onResume()
        Log.v("myTag", "onResume Traffic was called")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        /*outState.putString(REPORT_TITLE, binding.editTextReportTitle.text.toString())
        outState.putString(REPORT_DATE, binding.editTextReportDate.text.toString())
        outState.putString(REPORT_LOCATION, binding.editTextReportLocation.text.toString())
        outState.putString(REPORT_DESC, binding.editTextReportDesc.text.toString())*/
        Log.v("myTag", "onSaveInstanceState Traffic was called")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v("myTag", "onDestroy Traffic was called")

    }



    private fun setUpReportType(){
        binding.editableBoxesTrafficReport.reportType.apply{
            setContent {
                X9Theme {
                    val list = listOf(
                        "Speed Camera",
                        "Heavy Traffic",
                        "Road Incidents",
                        "Broken Vehicles",
                        "Other"
                    )
                    DropdownMenu(list, viewModel, true)
                }
            }
        }
    }
    private fun setUpSeverity(){
        binding.editableBoxesTrafficReport.severity.apply{
            setContent {
                X9Theme {
                    val list = listOf("Minor", "Moderate", "Major")
                    DropdownMenu(list, viewModel, false)
                }
            }
        }
    }
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        if (key == LocationTrackingPreferences.KEY_TRACKING_ENABLED) {
            LocationTrackingPreferences.isTrackingEnabled(requireContext())

        }
    }
    private fun setupUI() {
        with(binding){

            editableBoxesTrafficReport.editTextReportDate.setOnKeyListener { _, keyCode, event ->
                when {

                    //Check if it is the Enter-Key,      Check if the Enter Key was pressed down
                    ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {


                        //perform an action here e.g. a send message button click
                        Log.v("myTag", editableBoxesTrafficReport.editTextReportDate.text.toString())

                        //return true
                        return@setOnKeyListener true
                    }
                    else -> false
                }



            }


            editableBoxesTrafficReport.editTextReportDesc.setOnKeyListener { _, keyCode, event ->
                when {

                    //Check if it is the Enter-Key,      Check if the Enter Key was pressed down
                    ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {


                        //perform an action here e.g. a send message button click
                        Log.v("myTag", editableBoxesTrafficReport.editTextReportDesc.text.toString())

                        //return true
                        return@setOnKeyListener true
                    }
                    else -> false
                }



            }
            editableBoxesTrafficReport.editTextReportTitle.setOnKeyListener { _, keyCode, event ->
                when {

                    //Check if it is the Enter-Key,      Check if the Enter Key was pressed down
                    ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {


                        //perform an action here e.g. a send message button click
                        Log.v("myTag", editableBoxesTrafficReport.editTextReportTitle.text.toString())

                        //return true
                        return@setOnKeyListener true
                    }
                    else -> false
                }



            }

            buttonSend.setOnClickListener {
                if (editableBoxesTrafficReport.editTextReportDate.text.toString()!="" && editableBoxesTrafficReport.editTextReportDesc.text.toString()!="" && editableBoxesTrafficReport.editTextReportTitle.text.toString()!="" &&  viewModel.uiState.value.reportType!="Select report type" && viewModel.uiState.value.severity !="Select severity"){
                    Log.d("myTag", "Date: " + editableBoxesTrafficReport.editTextReportDate.text.toString() + "; Desc: " + editableBoxesTrafficReport.editTextReportDesc.text.toString() + "; Title: " + editableBoxesTrafficReport.editTextReportTitle.text.toString() + "; Type: " + viewModel.uiState.value.reportType + "; Severity: " + viewModel.uiState.value.severity)
                    viewModel.setTitle(editableBoxesTrafficReport.editTextReportTitle.text.toString())
                    val currentUserId = repository.currentUserId()
                    if (editableBoxesTrafficReport.editTextReportTitle.text.toString().trim().isNotEmpty() && currentUserId != null) {
                        val model = TrafficReportModel(
                            title = editableBoxesTrafficReport.editTextReportTitle.text.toString(),
                            date = editableBoxesTrafficReport.editTextReportDate.text.toString(),
                            reportType = viewModel.uiState.value.reportType,
                            severity = viewModel.uiState.value.severity,
                            desc = editableBoxesTrafficReport.editTextReportDesc.text.toString(),
                            createdAt = System.currentTimeMillis(),
                            latitude = latitude,
                            longitude = longitude,
                            image = "${repository.currentUserId()}/${viewModel.filename.value}",
                            landscape = viewModel.isLandscape.value
                        )
                        repository.addTrafficReport(
                            trafficReport = model,
                            userId = currentUserId,
                            image = viewModel.imageUri.value,
                            filename = viewModel.filename.value
                        )
                        viewModel.onImageUriChanged(null)
                        viewModel.onLandscapeChanged(null)
                        viewModel.onFilenameChanged(null)
                    }


                    showToast("Traffic Report Created!")
                }else{
                    showToast("Please fill out the Traffic Report")
                }

            }
            buttonTakePhoto.setOnClickListener {
                findNavController().navigate(
                    R.id.action_report_to_camera
                )
            }


        }}


}