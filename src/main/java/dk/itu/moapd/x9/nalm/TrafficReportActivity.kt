package dk.itu.moapd.x9.nalm

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dk.itu.moapd.x9.nalm.R
import dk.itu.moapd.x9.nalm.databinding.ActivityMainBinding
import dk.itu.moapd.x9.nalm.databinding.ActivityTrafficReportBinding

class TrafficReportActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTrafficReportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTrafficReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var intent = Intent(this@TrafficReportActivity, MainActivity::class.java)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.contentTrafficReport.buttonBackToHome.setOnClickListener{
            startActivity(intent)
        }

        binding.contentTrafficReport.buttonSend.setOnClickListener {
            intent = Intent(this@TrafficReportActivity, MainActivity::class.java).apply {
                putExtra("trafficType", binding.contentTrafficReport.editTextTrafficReportType.text.toString())
                putExtra("trafficReportDesc", binding.contentTrafficReport.editTextTrafficReportDesc.text.toString())
                putExtra("severity", binding.contentTrafficReport.autoCompleteTextViewTrafficSeverity.text.toString())
            }
        }
        setSeverity()
    }
    fun setSeverity() {
        val severity = resources.getStringArray(R.array.report_severity)
        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        val arrayAdapterSeverity = ArrayAdapter(this, R.layout.item_dropdown_type_report, severity)
        // get reference to the autocomplete text view
        val autocompleteTVSeverity = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextViewTrafficSeverity)
        // set adapter to the autocomplete tv to the arrayAdapter
        autocompleteTVSeverity.setAdapter(arrayAdapterSeverity)
    }
}