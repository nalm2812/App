package dk.itu.moapd.x9.nalm.app

import android.app.Application
import com.google.android.material.color.DynamicColors
import com.google.firebase.Firebase
import com.google.firebase.database.database
import dk.itu.moapd.x9.nalm.core.DATABASE_URL
import dk.itu.moapd.x9.nalm.core.preferences.LocationTrackingPreferences

class X9Application : Application() {
    override fun onCreate() {
        super.onCreate()

        // Apply dynamic colors to activities if available.
        DynamicColors.applyToActivitiesIfAvailable(this)
        LocationTrackingPreferences.setTrackingEnabled(this, false)


        // Enable disk persistence and keep the root reference synchronized.
        Firebase.database(DATABASE_URL).setPersistenceEnabled(true)
        Firebase.database(DATABASE_URL).reference.keepSynced(true)
    }
}