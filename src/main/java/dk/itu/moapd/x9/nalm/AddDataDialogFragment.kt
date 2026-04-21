package dk.itu.moapd.x9.nalm

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import dk.itu.moapd.x9.nalm.databinding.FragmentTrafficBinding
import dk.itu.moapd.x9.nalm.databinding.ListItemTrafficReportBinding

class AddDataDialogFragment : DialogFragment() {
    private var _binding: ListItemTrafficReportBinding?= null

    val binding
        get() =
            requireNotNull(_binding) {
                "Cannot access binding because it is null. Is the view visible?"
            }

    private val repository by lazy { TrafficReportRepository() }

    /*override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        // Inflate the view using view binding.
        _binding = FragmentTrafficBinding.inflate(layoutInflater)

        // Create a lambda for positive button click handling.
        val onPositiveButtonClick: (DialogInterface, Int) -> Unit = { dialog, _ ->
            val name = binding.editTextName.text.toString().trim()
            val userId = repository.currentUserId()

            if (name.isNotEmpty() && userId != null) {
                repository.addTrafficReport(
                    userId = userId,
                    name = name
                )
            }
            dialog.dismiss()
        }*/
}