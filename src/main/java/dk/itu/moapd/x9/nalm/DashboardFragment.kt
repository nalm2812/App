package dk.itu.moapd.x9.nalm

import android.R.attr.resource
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import dk.itu.moapd.x9.nalm.R
import dk.itu.moapd.x9.nalm.databinding.FragmentDashboardBinding
import dk.itu.moapd.x9.nalm.showToast
import android.content.Intent
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import dk.itu.moapd.x9.nalm.ui.theme.X9Theme
import kotlin.getValue


/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private val binding by viewBinding(FragmentDashboardBinding::bind)
    private val viewModel: MainViewModel by activityViewModels()
    private var trafficReportList: List<TrafficReportModel> = emptyList()

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
                val key = adapter?.getRef(position)?.key ?: return@TrafficReportModelLongClickListener
                UpdateDataDialogFragment
                    .createInstance(key=key, currentName = trafficReport.title, createdAt = trafficReport.createdAt, currentSeverity = trafficReport.severity, currentReportType = trafficReport.reportType, currentLocation = trafficReport.location, currentDate = trafficReport.date, currentDesc = trafficReport.desc)
                    .apply {isCancelable = false}
                    .show(parentFragmentManager, tag())
            },
            options = options,
        )
        //val adapter = TrafficReportAdapter(this, options)

        setupRecyclerView(requireNotNull(adapter))


        /*FirebaseAuth.getInstance().currentUser?.let { user ->
            val query = Firebase.database(DATABASE_URL).reference
                .child("trafficreport")
                .child(user.uid)
                .orderByChild("createdAt")
            val options = FirebaseRecyclerOptions.Builder<TrafficReportModel>()
                .setQuery(query, TrafficReportModel::class.java)
                .setLifecycleOwner(this)
                .build()
            val adapter = TrafficReportAdapter(
                longClickListener = TrafficReportModelLongClickListener { trafficReport, position ->
                    val key = adapter?.getRef(position)?.key ?: return@TrafficReportModelLongClickListener
                    UpdateDataDialogFragment
                        .createInstance(key=key, currentName = trafficReport.title, createdAt = trafficReport.createdAt)
                        .apply {isCancelable = false}
                        .show(parentFragmentManager, tag())
                },
                options = options,
            )
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
            }
        }*/


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
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
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