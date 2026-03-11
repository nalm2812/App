package dk.itu.moapd.x9.nalm

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
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
        setReportType(view)
        Log.v("myTag", "onCreateView Traffic was called")

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
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
                    showToast("Traffic Report Created!")
                }
                showToast("Please fill out Traffic Report")

            }
            buttonBackToHome.setOnClickListener {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_dashboard, DashboardFragment())
                    commit()
                }
            }

        }}
}