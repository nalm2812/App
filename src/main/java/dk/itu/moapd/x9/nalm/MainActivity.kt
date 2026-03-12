package dk.itu.moapd.x9.nalm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dk.itu.moapd.x9.nalm.databinding.ActivityMainBinding
import dk.itu.moapd.x9.nalm.DashboardFragment
import android.content.res.Configuration
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        //setupUI()
        /*if (savedInstanceState==null){
            val dashboard = DashboardFragment()
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_dashboard, dashboard)
                commit()
            }
        }*/
        val navController =
            (
                    supportFragmentManager.findFragmentById(R.id.fragment_container_view)
                            as NavHostFragment
                    ).navController

        // Define the AppBarConfiguration with the navController's graph.
        appBarConfiguration = AppBarConfiguration(navController.graph)

        // Setup the bottom navigation (portrait) and the navigation rail (landscape).
        //setupActionBarIfPortrait(navController)
        setupNavigation(navController)
        // Setup the action bar only in the portrait mode.


        Log.v("myTag", "onCreate was called")

    }

    private fun setupActionBarIfPortrait(navController: androidx.navigation.NavController) {
        if (resources.configuration.orientation != Configuration.ORIENTATION_PORTRAIT) return

        //setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    /**
     * Sets up the navigation components (bottom navigation and navigation rail) with the
     * NavController.
     *
     * @param navController The NavController to be used for navigation.
     */
    private fun setupNavigation(navController: androidx.navigation.NavController) {
        // Portrait: bottom navigation. Landscape: navigation rail.
        binding.contentMain.bottomNavigation?.setupWithNavController(navController)
        //binding.navigationRail?.setupWithNavController(navController)
    }

    /**
     * This method is called whenever the user chooses to navigate Up within your application's
     * activity hierarchy from the action bar.
     *
     * If a parent was specified in the manifest for this activity or an activity-alias to it,
     * default Up navigation will be handled automatically. See `getSupportParentActivityIntent()`
     * for how to specify the parent. If any activity along the parent chain requires extra `Intent`
     * arguments, the `Activity` subclass should override the method
     * `onPrepareSupportNavigateUpTaskStack(androidx.core.app.TaskStackBuilder)` to supply those
     * arguments.
     *
     * @return `true` if Up navigation completed successfully and this `Activity` was finished,
     *      `false` otherwise.
     */
    override fun onSupportNavigateUp(): Boolean {
        val navController =
            (
                    supportFragmentManager.findFragmentById(R.id.fragment_container_view)
                            as NavHostFragment
                    ).navController
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    /*private fun setupUI() {
        with(binding){
            contentMain.autoCompleteTextViewReportType.setOnDismissListener {
                Log.i("myTag", contentMain.autoCompleteTextViewReportType.text.toString())
            }
            contentMain.autoCompleteTextViewSeverity.setOnDismissListener {
                Log.i("myTag", contentMain.autoCompleteTextViewSeverity.text.toString())
            }
            contentMain.editTextReportDate.setOnKeyListener { v, keyCode, event ->
                when {

                    //Check if it is the Enter-Key,      Check if the Enter Key was pressed down
                    ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {


                        //perform an action here e.g. a send message button click
                        Log.v("myTag", contentMain.editTextReportDate.text.toString())

                        //return true
                        return@setOnKeyListener true
                    }
                    else -> false
                }



            }
            var desc = intent.getStringExtra("trafficReportDesc")
            if (desc!=null) {
                contentMain.editTextReportDesc.setText(desc)
            }

            contentMain.editTextReportDesc.setOnKeyListener { v, keyCode, event ->
                when {

                    //Check if it is the Enter-Key,      Check if the Enter Key was pressed down
                    ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {


                        //perform an action here e.g. a send message button click
                        Log.v("myTag", contentMain.editTextReportDesc.text.toString())

                        //return true
                        return@setOnKeyListener true
                    }
                    else -> false
                }



            }
            contentMain.editTextReportTitle.setOnKeyListener { v, keyCode, event ->
                when {

                    //Check if it is the Enter-Key,      Check if the Enter Key was pressed down
                    ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {


                        //perform an action here e.g. a send message button click
                        Log.v("myTag", contentMain.editTextReportTitle.text.toString())

                        //return true
                        return@setOnKeyListener true
                    }
                    else -> false
                }



            }
            contentMain.editTextReportLocation.setOnKeyListener { v, keyCode, event ->
                when {

                    //Check if it is the Enter-Key,      Check if the Enter Key was pressed down
                    ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {


                        //perform an action here e.g. a send message button click
                        Log.v("myTag", contentMain.editTextReportLocation.text.toString())

                        //return true
                        return@setOnKeyListener true
                    }
                    else -> false
                }



            }
            contentMain.buttonSend.setOnClickListener {
                if (contentMain.editTextReportDate.text.toString()!="" && contentMain.editTextReportDesc.text.toString()!="" && contentMain.editTextReportTitle.text.toString()!="" && contentMain.editTextReportLocation.text.toString()!="" && contentMain.autoCompleteTextViewReportType.text.toString()!="Select report type" && contentMain.autoCompleteTextViewSeverity.text.toString() !="Select severity"){
                    Log.d("myTag", "Date: " + contentMain.editTextReportDate.text.toString() + "; Desc: " + contentMain.editTextReportDesc.text.toString() + "; Title: " + contentMain.editTextReportTitle.text.toString() + "; Location: " + contentMain.editTextReportLocation.text.toString() + "; Type: " + contentMain.autoCompleteTextViewReportType.text.toString() + "; Severity: " + contentMain.autoCompleteTextViewSeverity.text.toString())
                }
            }
            contentMain.buttonTrafficReport.setOnClickListener {
                val intent = Intent(this@MainActivity, TrafficReportActivity::class.java)
                startActivity(intent)
            }

             fun setReportType(view: View?)  { //got the code from here: https://www.geeksforgeeks.org/kotlin/exposed-drop-down-menu-in-android/
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
    fun setSeverity(view: View?) { //got the code from here: https://www.geeksforgeeks.org/kotlin/exposed-drop-down-menu-in-android/
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

    }}*/

    override fun onStart(){
        super.onStart()
        Log.v("myTag", "onStart was called")
    }

    override fun onResume(){
        super.onResume()
        Log.v("myTag", "onResume was called")
    }

    override fun onPause(){
        super.onPause()
        Log.v("myTag", "onPause was called")
    }

    override fun onStop(){
        super.onStop()
        Log.v("myTag", "onStop was called")
    }


}