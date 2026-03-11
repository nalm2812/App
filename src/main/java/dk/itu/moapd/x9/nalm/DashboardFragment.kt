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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private val binding by viewBinding(FragmentDashboardBinding::bind)


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
        binding.buttonTrafficReport.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_dashboard, TrafficFragment())
                commit()
            }
        }
        setupRecyclerView()
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

    private fun setupRecyclerView() =
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = CustomAdapter(createTrafficReport())

            ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
                val navBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
                view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    bottomMargin = navBarHeight
                }
                insets
            }
        }

    private fun createTrafficReport() : List<TrafficReportModel> =
        (1..10).map { index ->
            TrafficReportModel(
                title = "title",
                location = "location",
                date = "date",
                reportType = "report type",
                severity = "severity",
                desc = "description"
            )
        }




}