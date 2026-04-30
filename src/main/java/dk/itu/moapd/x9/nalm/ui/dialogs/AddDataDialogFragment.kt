package dk.itu.moapd.x9.nalm.ui.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dk.itu.moapd.x9.nalm.R
import dk.itu.moapd.x9.nalm.data.repository.TrafficReportRepository
import dk.itu.moapd.x9.nalm.databinding.ListItemTrafficReportBinding

class AddDataDialogFragment : DialogFragment() {
    private var _binding: ListItemTrafficReportBinding?= null//idk if this is right

    val binding
        get() =
            requireNotNull(_binding) {
                "Cannot access binding because it is null. Is the view visible?"
            }

    private val repository by lazy { TrafficReportRepository() }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        // Inflate the view using view binding.
        _binding = ListItemTrafficReportBinding.inflate(layoutInflater)

        // Create a lambda for positive button click handling.
        val onPositiveButtonClick: (DialogInterface, Int) -> Unit = { dialog, _ ->
            val name = binding.inputTrafficTitle.text.toString().trim()
            val desc = binding.inputTrafficDesc.text.toString().trim()
            val date = binding.inputTrafficDate.text.toString().trim()
            val location = binding.inputTrafficLocation.text.toString().trim()
            val severity = binding.inputTrafficSeverity.text.toString().trim()
            val reportType = binding.inputTrafficReportType.text.toString().trim()

            val userId = repository.currentUserId()

            if (name.isNotEmpty() && userId != null) {
                repository.addTrafficReport(
                    userId = userId,
                    title = name,
                    location = location,
                    date = date,
                    reportType = reportType,
                    severity = severity,
                    desc = desc

                )
            }
            dialog.dismiss()
        }
        return MaterialAlertDialogBuilder(requireContext()).apply {
            setView(binding.root)
            setTitle(getString(R.string.dialog_add_title))
            setMessage(getString(R.string.dialog_add_message))
            setPositiveButton(getString(R.string.button_add), onPositiveButtonClick)
            setNegativeButton(getString(R.string.button_cancel)) { dialog, _ -> dialog.dismiss() }
        }.create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}