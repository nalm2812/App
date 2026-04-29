package dk.itu.moapd.x9.nalm

import android.app.Application
import com.google.android.material.color.DynamicColors
import com.google.firebase.Firebase
import com.google.firebase.database.database

class X9Application : Application() {
    override fun onCreate() {
        super.onCreate()

        // Apply dynamic colors to activities if available.
        DynamicColors.applyToActivitiesIfAvailable(this)

        // Enable disk persistence and keep the root reference synchronized.
        Firebase.database(DATABASE_URL).setPersistenceEnabled(true)
        Firebase.database(DATABASE_URL).reference.keepSynced(true)
    }
}