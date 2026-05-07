package dk.itu.moapd.x9.nalm.data.repository

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import dk.itu.moapd.x9.nalm.core.BUCKET_URL
import dk.itu.moapd.x9.nalm.domain.model.TrafficReportModel
import dk.itu.moapd.x9.nalm.core.DATABASE_URL
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID
import kotlin.jvm.java

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
    fun addTrafficReport(trafficReport: TrafficReportModel, userId: String, image: Uri?) {
        val key = root
            .child(PATH_TRAFFIC_REPORTS)
            .child(userId)
            .push()
            .key ?: return

        if (image!=null){
            val filename = UUID.randomUUID().toString()
            val remotePath = "images/$userId/$filename"
            uploadImage(image, remotePath)
        }



        root
            .child(PATH_TRAFFIC_REPORTS)
            .child(userId)
            .child(key)
            .setValue(trafficReport)


    }

    fun updateTrafficReport(userId: String, key: String, title: String, location: String, date: String, reportType: String, severity: String, desc: String, now: Long = System.currentTimeMillis(), createdAt: Long?, latitude: Double?, longitude: Double?, image: String?){
        val trafficReport = TrafficReportModel(
            title = title,
            location = location,
            date = date,
            reportType = reportType,
            severity = severity,
            desc = desc,
            createdAt = createdAt,
            updatedAt = now,
            latitude = latitude,
            longitude = longitude,
            image = image
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

    private val storage = Firebase.storage(BUCKET_URL)
    fun uploadImage(localUri: Uri, remotePath: String): Task<Uri> {
        val ref: StorageReference = storage.reference.child(remotePath)
        return ref.putFile(localUri).continueWithTask { task ->
            if (!task.isSuccessful) {
                throw (task.exception ?: Exception("Upload failed"))
            }
            ref.downloadUrl
        }
    }
    fun deleteImage(remotePath: String): Task<Void> {
        return storage.reference.child(remotePath).delete()
    }
}