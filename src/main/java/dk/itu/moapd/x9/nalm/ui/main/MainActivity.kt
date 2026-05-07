package dk.itu.moapd.x9.nalm.ui.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.x9.nalm.ui.auth.LoginActivity
import dk.itu.moapd.x9.nalm.ui.main.MainViewModel
import dk.itu.moapd.x9.nalm.R
import dk.itu.moapd.x9.nalm.core.API_KEY
import dk.itu.moapd.x9.nalm.ui.dialogs.UserInfoDialogFragment
import dk.itu.moapd.x9.nalm.databinding.ActivityMainBinding
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

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
        setupActionBarIfPortrait(navController)
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser!=null){
            setupNavigation(navController)
        }
        //setupNavigation(navController)

        // Setup the action bar only in the portrait mode.
        getAirQuality()

        Log.v("myTag", "onCreate was called")

    }

    private fun setupActionBarIfPortrait(navController: NavController) {
        if (resources.configuration.orientation != Configuration.ORIENTATION_PORTRAIT) return

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    /**
     * Sets up the navigation components (bottom navigation and navigation rail) with the
     * NavController.
     *
     * @param navController The NavController to be used for navigation.
     */
    private fun setupNavigation(navController: NavController) {
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
        //auth.currentUser ?: startLoginActivity()
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

    private fun startLoginActivity() {
        Intent(this, LoginActivity::class.java).apply {
            // An alternative to instead of calling finish() method.
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }.let(::startActivity)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (auth.currentUser!=null){
            menuInflater.inflate(R.menu.top_app_bar, menu)
        }else{
            menuInflater.inflate(R.menu.top_app_bar_logged_out, menu)

        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        // Handle top app bar menu item clicks.
        R.id.action_user_info -> {
            UserInfoDialogFragment().apply {
                isCancelable = false
            }.also { dialogFragment ->
                dialogFragment.show(supportFragmentManager, "UserInfoDialogFragment")
            }
            true
        }
        R.id.action_logout -> {
            auth.signOut()
            startActivity(Intent(this@MainActivity, MainActivity::class.java))
            //startLoginActivity()
            true
        }
        R.id.action_login -> {
            startLoginActivity()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment_container_view)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    private fun getAirQuality(){
        val url = "https://airquality.googleapis.com/v1/currentConditions:lookup?key=${API_KEY}"
        val requestQueue = Volley.newRequestQueue(this)

        val postdata2 = JSONObject()
        postdata2.put("latitude", "37.419734")
        postdata2.put("longitude", "-122.0827784")
        val postdata = JSONObject()
        postdata.put("location", postdata2)




        val stringRequest = object : JsonObjectRequest(Request.Method.POST, url,
            postdata, Response.Listener { response ->
                try {
                    val indexes = response.getJSONArray("indexes")
                    val item = indexes.getJSONObject(0)
                    val aqi = item.getString("aqi")
                    Log.v("testing", aqi)
                } catch (e: Exception) {
                    Log.v("testing", "ERROR ERROR 2")
                }

            },
            Response.ErrorListener { error ->
                Log.v("testing", "ERROR ERROR 1")
                error.printStackTrace()
            }) {

        }

        requestQueue.add(stringRequest)
    }

}