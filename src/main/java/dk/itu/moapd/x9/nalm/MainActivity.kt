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



class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

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
        val dashboard = DashboardFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_dashboard, dashboard)
            commit()
        }
        Log.v("myTag", "onCreate was called")

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