package dk.itu.moapd.x9.nalm.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * Helper object responsible for camera permission checks.
 *
 * Keeps permission-related logic outside UI classes (Fragments/Activities).
 */
object CameraPermissionHelper {

    /**
     * Returns true if the app has permission to use the camera.
     *
     * @param context The application context.
     *
     * @return True if the app has permission to use the camera, false otherwise.
     */
    fun hasCameraPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA,
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * The camera permission string.
     *
     * Useful to avoid duplicating Manifest.permission.CAMERA everywhere.
     */
    const val CAMERA_PERMISSION: String = Manifest.permission.CAMERA
}