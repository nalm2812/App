package dk.itu.moapd.x9.nalm

import android.R.attr.fontWeight
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dk.itu.moapd.x9.nalm.ui.theme.X9Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrafficReportItem(trafficReport: TrafficReportModel, modifier: Modifier = Modifier) {
    Card(modifier = modifier.padding(8.dp).fillMaxWidth()) {
        Row{
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Title:",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis)
                Text(
                    text = "Location:",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis)
                Text(
                    text = "Date:",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis)
                Text(
                    text = "Report Type:",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis)
                Text(
                    text = "Severity:",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis)
                Text(
                    text = "Report Description:",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis)
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = trafficReport.title,
                    fontSize = 15.sp,
                    overflow = TextOverflow.Ellipsis
                    )
                Text(
                    text = trafficReport.location,
                    fontSize = 15.sp,
                    overflow = TextOverflow.Ellipsis)
                Text(
                    text = trafficReport.date,
                    fontSize = 15.sp,
                    overflow = TextOverflow.Ellipsis)
                Text(
                    text = trafficReport.reportType,
                    fontSize = 15.sp,
                    overflow = TextOverflow.Ellipsis)
                Text(
                    text = trafficReport.severity,
                    fontSize = 15.sp,
                    overflow = TextOverflow.Ellipsis)
                Text(
                    text = trafficReport.desc,
                    fontSize = 15.sp,
                    overflow = TextOverflow.Ellipsis)
            }
        }



        }
    }

@Preview
@Composable
fun TrafficReportItemPreview() {
    TrafficReportItem(TrafficReportModel(
        title = "title",
        location = "locatoin",
        date = "date",
        reportType = "reportType",
        severity = "severity",
        desc = "hello"
    ))
}
