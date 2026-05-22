package dk.itu.moapd.x9.nalm.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request.Method
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.x9.nalm.ui.auth.LoginActivity
import dk.itu.moapd.x9.nalm.R
import dk.itu.moapd.x9.nalm.core.API_KEY
import dk.itu.moapd.x9.nalm.ui.dialogs.UserInfoDialogFragment
import dk.itu.moapd.x9.nalm.databinding.ActivityMainBinding
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navController =
            (
                    supportFragmentManager.findFragmentById(R.id.fragment_container_view)
                            as NavHostFragment
                    ).navController

        // Define the AppBarConfiguration with the navController's graph.
        appBarConfiguration = AppBarConfiguration(navController.graph)

        // Setup the bottom navigation (portrait) and the navigation rail (landscape).
        setupActionBar(navController)
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser!=null){
            setupNavigation(navController)
        }




        Log.v("myTag", "onCreate was called")

    }

    private fun setupActionBar(navController: NavController) {

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
        binding.navigationRail?.setupWithNavController(navController)    }

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




    override fun onStart(){
        super.onStart()
        getAndUpdateAirQuality()
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

    private fun getAndUpdateAirQuality(){

        val url = "https://airquality.googleapis.com/v1/currentConditions:lookup?key=${API_KEY}"
        val requestQueue = Volley.newRequestQueue(this)

        val postdata2 = JSONObject()
        postdata2.put("latitude", 0)
        postdata2.put("longitude", 1)
        val postdata = JSONObject()
        postdata.put("location", postdata2)




        val stringRequest = object : JsonObjectRequest(Method.POST, url,
            postdata, Response.Listener { response ->
                try {
                    val indexes = response.getJSONArray("indexes")
                    val item = indexes.getJSONObject(0)
                    val aqi = item.getString("aqi")
                    val category = item.getString("category")
                    //binding.contentMain.header?.inputAirQualityIndex?.text = aqi
                    //binding.contentMain.header?.inputAirQualityCategory?.text = category
                    //binding.contentMain.header?.inputAirQualityIndex?.text = "testing"
                    //binding.contentMain.header?.inputAirQualityCategory?.text = "testing"
                } catch (e: Exception) {
                    Log.v("testing", "ERROR ERROR 2")
                    e.printStackTrace()
                }

            },
            Response.ErrorListener { error ->
                Log.v("testing", "ERROR ERROR 1")
                error.printStackTrace()
            }) {

        }

        stringRequest.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        requestQueue.add(stringRequest)


    }


}