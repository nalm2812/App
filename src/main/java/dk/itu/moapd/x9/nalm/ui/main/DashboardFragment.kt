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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.database.database
import dk.itu.moapd.x9.nalm.core.DATABASE_URL
import dk.itu.moapd.x9.nalm.ui.main.MainViewModel
import dk.itu.moapd.x9.nalm.R
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


/**
 * A simple [androidx.fragment.app.Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private val binding by viewBinding(FragmentDashboardBinding::bind)
    private val viewModel: MainViewModel by activityViewModels()

    private val repository by lazy { TrafficReportRepository() }

    private var adapter: TrafficReportAdapter? = null







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
                        image = trafficReport.image
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








}


