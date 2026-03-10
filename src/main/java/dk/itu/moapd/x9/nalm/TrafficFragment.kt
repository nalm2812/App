package dk.itu.moapd.x9.nalm

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import dk.itu.moapd.x9.nalm.databinding.FragmentDashboardBinding
import dk.itu.moapd.x9.nalm.databinding.FragmentDashboardBinding.bind
import dk.itu.moapd.x9.nalm.databinding.FragmentTrafficBinding


class TrafficFragment : Fragment(R.layout.fragment_traffic) {
    private val binding by viewBinding(FragmentTrafficBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = super.onCreateView(inflater, container, savedInstanceState)
        setSeverity(view)
        Log.v("myTag", "onCreateView Traffic was called")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonBackToHome.setOnClickListener{
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_dashboard, DashboardFragment())
                commit()
            }
        }
        binding.buttonSend.setOnClickListener {
            showToast("Traffic Report Sent!")
        }
        Log.v("myTag", "onViewCreated Traffic was called")

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("myTag", "onCreate Traffic was called")
    }

    override fun onStart() {
        super.onStart()
        Log.v("myTag", "onStart Traffic was called")
    }

    override fun onStop() {
        super.onStop()
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
        Log.v("myTag", "onSaveInstanceState Traffic was called")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v("myTag", "onDestroy Traffic was called")

    }


    fun setSeverity(view: View?) {
        val severity = resources.getStringArray(R.array.report_severity)
        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        val arrayAdapterSeverity = ArrayAdapter(requireActivity(), R.layout.item_dropdown_type_report, severity)
        // get reference to the autocomplete text view
        val autocompleteTVSeverity = view?.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextViewTrafficSeverity)
        // set adapter to the autocomplete tv to the arrayAdapter

        autocompleteTVSeverity?.setAdapter(arrayAdapterSeverity)

    }
}