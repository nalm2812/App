package dk.itu.moapd.x9.nalm

import android.widget.Toast
import androidx.fragment.app.Fragment

class Toast {
    fun Fragment.showToast(
        message: CharSequence,
        duration: Int = Toast.LENGTH_SHORT,
    ) {
        Toast.makeText(requireContext(), message, duration).show()
    }
}