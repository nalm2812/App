package dk.itu.moapd.x9.nalm

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.database

class TrafficReportRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val root: DatabaseReference = Firebase.database(DATABASE_URL).reference,
) {
    companion object {
        private const val PATH_TRAFFIC_REPORTS = "trafficreport"
        private const val CHILD_CREATED_AT = "createdAt"
    }
    fun currentUserId(): String? = auth.currentUser?.uid
    fun trafficReportQuery(userId: String): Query = root
        .child(PATH_TRAFFIC_REPORTS)
        .child(userId)
        .orderByChild(CHILD_CREATED_AT)
    fun addTrafficReport(userId: String, title: String, location: String, date: String, reportType: String, severity: String, desc: String, now: Long = System.currentTimeMillis()) {
        val key = root
            .child(PATH_TRAFFIC_REPORTS)
            .child(userId)
            .push()
            .key ?: return
        val trafficReport = TrafficReportModel(
            title = title,
            location = location,
            date = date,
            reportType = reportType,
            severity = severity,
            desc = desc,
            createdAt = now
        )
        root
            .child(PATH_TRAFFIC_REPORTS)
            .child(userId)
            .child(key)
            .setValue(trafficReport)


    }

    fun updateTrafficReport(userId: String, key: String, title: String, location: String, date: String, reportType: String, severity: String, desc: String, now: Long = System.currentTimeMillis(), createdAt: Long?){
        val trafficReport = TrafficReportModel(
            title = title,
            location = location,
            date = date,
            reportType = reportType,
            severity = severity,
            desc = desc,
            createdAt = createdAt,
            updatedAt = now
        )
        root
            .child(PATH_TRAFFIC_REPORTS)
            .child(userId)
            .child(key)
            .setValue(trafficReport)
    }

    fun deleteTrafficReport(userId: String, key: String){
        root
            .child(PATH_TRAFFIC_REPORTS)
            .child(userId)
            .child(key)
            .removeValue()
    }
}