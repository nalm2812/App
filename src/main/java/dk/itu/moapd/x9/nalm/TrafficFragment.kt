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
import androidx.fragment.app.activityViewModels
import dk.itu.moapd.x9.nalm.databinding.FragmentTrafficBinding
import dk.itu.moapd.x9.nalm.ui.theme.X9Theme
import kotlin.getValue


class TrafficFragment : Fragment(R.layout.fragment_traffic) {
    private val binding by viewBinding(FragmentTrafficBinding::bind)
    private val viewModel: MainViewModel by activityViewModels()


    companion object {
        private const val REPORT_TITLE = "report_title"
        private const val REPORT_LOCATION = "report_location"
        private const val REPORT_DATE = "report_date"
        private const val REPORT_DESC = "report_desc"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
        Log.v("myTag", "onCreateView Traffic was called")

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        //setSeverity(view)
        //setReportType(view)
        setUpReportType()
        setUpSeverity()
        Log.v("myTag", "onViewCreated Traffic was called")

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("myTag", "onCreate Traffic was called")
        //restoreState(savedInstanceState)
    }


    private fun restoreState(savedInstanceState: Bundle?){
        if (savedInstanceState==null) return
        val title = savedInstanceState?.getString(REPORT_TITLE, null)
        val location = savedInstanceState?.getString(REPORT_LOCATION, null)

        val date = savedInstanceState?.getString(REPORT_DATE, null)
        val desc = savedInstanceState?.getString(REPORT_DESC, null)

        if (title!=null){
            Log.v("myTag", "are you here1?: " + title)
            binding.editTextReportTitle.setText(title)

        }
        if (location!=null){
            Log.v("myTag", "are you here?2")

            binding.editTextReportLocation.setText(location)

        }
        if (date!=null){
            Log.v("myTag", "are you here?3")

            binding.editTextReportDate.setText(date)

        }
        if (desc!=null){
            Log.v("myTag", "are you here?4")

            binding.editTextReportDesc.setText(desc)

        }
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


    /*fun setReportType(view: View?)  {
        Log.v("myTag", "reportType was called")
        val reportType = resources.getStringArray(R.array.report_types)
        val arrayAdapterReportType = ArrayAdapter(requireActivity(), R.layout.item_dropdown_type_report, reportType)
        val autocompleteTVReportType = view?.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextViewReportType)
        autocompleteTVReportType?.setAdapter(arrayAdapterReportType)

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

    }*/

    private fun setUpReportType(){
        binding.reportType.apply{
            setContent {
                X9Theme {
                    val list = listOf("Speed Camera", "Heavy Traffic", "Road Incidents", "Broken Vehicles", "Other")
                    DropdownMenu(list,  viewModel, true)
                }
            }
        }
    }
    private fun setUpSeverity(){
        binding.severity.apply{
            setContent {
                X9Theme {
                    val list = listOf("Minor", "Moderate", "Major")
                    DropdownMenu(list,  viewModel, false)
                }
            }
        }
    }
    private fun setupUI() {
        with(binding){
            /*autoCompleteTextViewReportType.setOnDismissListener {
                Log.i("myTag", autoCompleteTextViewReportType.text.toString())
            }
            autoCompleteTextViewSeverity.setOnDismissListener {
                Log.i("myTag", autoCompleteTextViewSeverity.text.toString())
            }*/
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
                if (editTextReportDate.text.toString()!="" && editTextReportDesc.text.toString()!="" && editTextReportTitle.text.toString()!="" && editTextReportLocation.text.toString()!="" && viewModel.uiState.value.reportType!="Select report type" && viewModel.uiState.value.severity !="Select severity"){
                    Log.d("myTag", "Date: " + editTextReportDate.text.toString() + "; Desc: " + editTextReportDesc.text.toString() + "; Title: " + editTextReportTitle.text.toString() + "; Location: " + editTextReportLocation.text.toString() + "; Type: " + viewModel.uiState.value.reportType + "; Severity: " + viewModel.uiState.value.severity)
                    viewModel.setTitle(editTextReportTitle.text.toString())
                    viewModel.addItem(TrafficReportModel(
                        title = editTextReportTitle.text.toString(),
                        location = editTextReportLocation.text.toString(),
                        date = editTextReportDate.text.toString(),
                        reportType = viewModel.uiState.value.reportType,
                        severity = viewModel.uiState.value.severity,
                        desc = editTextReportDesc.text.toString()
                    ))
                    showToast("Traffic Report Created!")
                }else{
                    showToast("Please fill out the Traffic Report")
                }

            }


        }}
}