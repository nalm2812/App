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
        val view = super.onCreateView(inflater, container, savedInstanceState)
        setSeverity(view)
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
    }


    fun setSeverity(view: View?) {
        val severity = resources.getStringArray(R.array.report_severity)
        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        val arrayAdapterSeverity = ArrayAdapter(requireActivity(), R.layout.item_dropdown_type_report, severity)
        // get reference to the autocomplete text view
        val autocompleteTVSeverity = view?.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextViewSeverity)
        // set adapter to the autocomplete tv to the arrayAdapter

        autocompleteTVSeverity?.setAdapter(arrayAdapterSeverity)

    }
}