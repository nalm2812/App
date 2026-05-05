package dk.itu.moapd.x9.nalm.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.x9.nalm.ui.main.MainViewModel
import dk.itu.moapd.x9.nalm.R
import dk.itu.moapd.x9.nalm.core.tag
import dk.itu.moapd.x9.nalm.databinding.FragmentMapBinding
import dk.itu.moapd.x9.nalm.ui.utils.viewBinding
import kotlin.getValue

class MapFragment : Fragment(R.layout.fragment_map){
    private val binding by viewBinding(FragmentMapBinding::bind)
    private val viewModel: MainViewModel by activityViewModels()




    /**
     * The Google Maps object.
     */
    private var googleMap: GoogleMap? = null

    /**
     * Activity Result API launcher for requesting location permission.
     * When permission is granted, immediately starts location tracking.
     */

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            enableMyLocation()
        } else {
            // Use view (nullable) to avoid crashes if view is destroyed
            view?.let {
                Snackbar.make(
                    it,
                    R.string.permission_denied_message,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    /**
     * Manipulates the map once available. This callback is triggered when the map is ready to be
     * used. This is where we can add markers or lines, add listeners or move the camera. In this
     * case, we just add a marker near IT University Copenhagen, Denmark. If Google Play services
     * is not installed on the device, the user will be prompted to install it inside the
     * SupportMapFragment. This method will only be triggered once the user has installed Google
     * Play services and returned to the app.
     */
    private val callback = OnMapReadyCallback { googleMap ->

        // Update the Google Maps object.
        this.googleMap = googleMap

        // We use the view's root to find out how big the system bars are.
        view?.let { fragmentView ->
            ViewCompat.setOnApplyWindowInsetsListener(fragmentView) { _, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

                // It automatically pushes UI buttons below the status bar and above the navigation
                // bar.
                googleMap.setPadding(0, systemBars.top, 0, systemBars.bottom)

                insets
            }
            ViewCompat.requestApplyInsets(fragmentView)
        }



        val list = viewModel.items.value
        Log.v("testing", "list size: " + list?.size)

        if (list != null) {
            for (report in list) {
                if (report.latitude!=null && report.longitude!=null){
                    val marker = googleMap.addMarker(
                        MarkerOptions().position(LatLng(report.latitude, report.longitude)).title(report.title)
                    )
                    marker?.tag = report
                }

            }
        }
        val itu = LatLng(55.6596, 12.5910)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(itu))

        // Set the Google Maps style.
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.maps_style_json)
        )

        // Enable the location layer. Request the permission if it is not granted.
        if (checkPermission()) {
            @Suppress("MissingPermission")
            googleMap.isMyLocationEnabled = true
        } else {
            requestUserPermissions()
        }

    }

    /**
     * Called immediately after `onCreateView(LayoutInflater, ViewGroup, Bundle)` has returned, but
     * before any saved state has been restored in to the view. This gives subclasses a chance to
     * initialize themselves once they know their view hierarchy has been completely created. The
     * fragment's view hierarchy is not however attached to its parent at this point.
     *
     * @param view The View returned by `onCreateView(LayoutInflater, ViewGroup, Bundle)`.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *      saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager
            .findFragmentById(binding.map.id) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    /**
     * This method checks if the user allows the application uses all location-aware resources to
     * monitor the user's location.
     *
     * @return A boolean value with the user permission agreement.
     */
    private fun checkPermission() =
        ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    /**
     * Create a set of dialogs to show to the users and ask them for permissions to get the device's
     * resources.
     */
    private fun requestUserPermissions() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private fun enableMyLocation() {
        try {
            if (checkPermission()) {
                googleMap?.isMyLocationEnabled = true
            }
        } catch (e: SecurityException) {
            Log.e(tag(), "Cannot enable location: ${e.message}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.updateReportList()
    }



}