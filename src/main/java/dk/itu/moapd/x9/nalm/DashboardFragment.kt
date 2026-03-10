package dk.itu.moapd.x9.nalm

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
        setReportType(view)
        setSeverity(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()

    }
    fun setReportType(view: View?)  {
        val reportType = resources.getStringArray(R.array.report_types)
        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        val arrayAdapterReportType = ArrayAdapter(requireActivity(), R.layout.item_dropdown_type_report, reportType)
        // get reference to the autocomplete text view
        val autocompleteTVReportType = view?.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextViewReportType)
        // set adapter to the autocomplete tv to the arrayAdapter
        val trafficType = arguments?.getString("trafficType")
        if (trafficType!=null){
            autocompleteTVReportType?.setText(trafficType)
        }
        autocompleteTVReportType?.setAdapter(arrayAdapterReportType)

    }
    fun setSeverity(view: View?) {
        val severityTraffic = arguments?.getString("severity")
        Log.v("myTag", "idk what im doing: " + severityTraffic)
        val severity = resources.getStringArray(R.array.report_severity)
        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        val arrayAdapterSeverity = ArrayAdapter(requireActivity(), R.layout.item_dropdown_type_report, severity)
        // get reference to the autocomplete text view
        val autocompleteTVSeverity = view?.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextViewSeverity)
        // set adapter to the autocomplete tv to the arrayAdapter
        if (severityTraffic!=null){
            autocompleteTVSeverity?.setText(severityTraffic)
        }
        autocompleteTVSeverity?.setAdapter(arrayAdapterSeverity)

    }

    private fun setupUI() {
        with(binding){
            autoCompleteTextViewReportType.setOnDismissListener {
                Log.i("myTag", autoCompleteTextViewReportType.text.toString())
            }
            autoCompleteTextViewSeverity.setOnDismissListener {
                Log.i("myTag", autoCompleteTextViewSeverity.text.toString())
            }
            editTextReportDate.setOnKeyListener { v, keyCode, event ->
                when {

                    //Check if it is the Enter-Key,      Check if the Enter Key was pressed down
                    ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {


                        //perform an action here e.g. a send message button click
                        Log.v("myTag", editTextReportDate.text.toString())

                        //return true
                        return@setOnKeyListener true
                    }
                    else -> false
                }



            }
            /*var desc = arguments?.getString("trafficReportDesc")
            if (desc!=null) {
                editTextReportDesc.setText(desc)
            }*/

            editTextReportDesc.setOnKeyListener { v, keyCode, event ->
                when {

                    //Check if it is the Enter-Key,      Check if the Enter Key was pressed down
                    ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {


                        //perform an action here e.g. a send message button click
                        Log.v("myTag", editTextReportDesc.text.toString())

                        //return true
                        return@setOnKeyListener true
                    }
                    else -> false
                }



            }
            editTextReportTitle.setOnKeyListener { v, keyCode, event ->
                when {

                    //Check if it is the Enter-Key,      Check if the Enter Key was pressed down
                    ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {


                        //perform an action here e.g. a send message button click
                        Log.v("myTag", editTextReportTitle.text.toString())

                        //return true
                        return@setOnKeyListener true
                    }
                    else -> false
                }



            }
            editTextReportLocation.setOnKeyListener { v, keyCode, event ->
                when {

                    //Check if it is the Enter-Key,      Check if the Enter Key was pressed down
                    ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {


                        //perform an action here e.g. a send message button click
                        Log.v("myTag", editTextReportLocation.text.toString())

                        //return true
                        return@setOnKeyListener true
                    }
                    else -> false
                }



            }
            buttonSend.setOnClickListener {
                if (editTextReportDate.text.toString()!="" && editTextReportDesc.text.toString()!="" && editTextReportTitle.text.toString()!="" && editTextReportLocation.text.toString()!="" && autoCompleteTextViewReportType.text.toString()!="Select report type" && autoCompleteTextViewSeverity.text.toString() !="Select severity"){
                    Log.d("myTag", "Date: " + editTextReportDate.text.toString() + "; Desc: " + editTextReportDesc.text.toString() + "; Title: " + editTextReportTitle.text.toString() + "; Location: " + editTextReportLocation.text.toString() + "; Type: " + autoCompleteTextViewReportType.text.toString() + "; Severity: " + autoCompleteTextViewSeverity.text.toString())
                }
                showToast("Report Created!")
            }
            buttonTrafficReport.setOnClickListener {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_dashboard, TrafficFragment())
                    commit()
                }
            }

        }}



}