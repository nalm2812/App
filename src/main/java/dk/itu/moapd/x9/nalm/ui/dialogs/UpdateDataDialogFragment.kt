package dk.itu.moapd.x9.nalm.ui.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dk.itu.moapd.x9.nalm.R
import dk.itu.moapd.x9.nalm.data.repository.TrafficReportRepository
import dk.itu.moapd.x9.nalm.databinding.EditableBoxesTrafficReportBinding
import dk.itu.moapd.x9.nalm.databinding.FragmentTrafficBinding

class UpdateDataDialogFragment : DialogFragment() {
    companion object {

        /**
         * The argument key for the key of the dummy.
         */
        private const val ARG_KEY = "arg_key"

        /**
         * The argument key for the name of the dummy.
         */
        private const val ARG_NAME = "arg_name"

        private const val ARG_SEVERITY = "arg_severity"
        private const val ARG_REPORT_TYPE = "arg_report_type"

        /**
         * The argument key for the creation time of the dummy.
         */
        private const val ARG_CREATED_AT = "arg_created_at"

        private const val ARG_DATE = "arg_date"
        private const val ARG_DESC = "arg_desc"
        private const val ARG_LATITUDE = "arg_latitude"
        private const val ARG_LONGITUDE = "arg_longitude"
        private const val ARG_IMAGE = "arg_image"
        private const val ARG_LANDSCAPE = "arg_landscape"


        /**
         * Creates a new instance of a [UpdateDataDialogFragment].
         *
         * @param key The key of the dummy.
         * @param currentName The current name of the dummy.
         * @param createdAt The creation time of the dummy.
         *
         * @return A new instance of a [UpdateDataDialogFragment].
         */
        fun createInstance(
            key: String,
            currentName: String,
            currentDate: String,
            currentReportType: String,
            currentSeverity: String,
            currentDesc: String,
            createdAt: Long?,
            latitude: Double?,
            longitude: Double?,
            image: String?,
            landscape: Boolean?

        ): UpdateDataDialogFragment {
            return UpdateDataDialogFragment().apply {
                arguments = bundleOf(
                    ARG_KEY to key,
                    ARG_NAME to currentName,
                    ARG_DATE to currentDate,
                    ARG_REPORT_TYPE to currentReportType,
                    ARG_SEVERITY to currentSeverity,
                    ARG_DESC to currentDesc,
                    ARG_CREATED_AT to (createdAt ?: Long.MIN_VALUE),
                    ARG_LATITUDE to (latitude ?: Double.MIN_VALUE),
                    ARG_LONGITUDE to (longitude ?: Double.MIN_VALUE),
                    ARG_IMAGE to (image ?: ""),
                    ARG_LANDSCAPE to (landscape)
                )
            }
        }
    }

    private var _binding: EditableBoxesTrafficReportBinding? = null

    val binding
        get() =
            requireNotNull(_binding) {
                "Cannot access binding because it is null. Is the view visible?"
            }

    private val repository by lazy { TrafficReportRepository() }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        // Get the arguments from the bundle.
        val key = requireArguments().getString(ARG_KEY)
        val currentName = requireArguments().getString(ARG_NAME).orEmpty()
        val currentSeverity = requireArguments().getString(ARG_SEVERITY).orEmpty()
        val currentReportType = requireArguments().getString(ARG_REPORT_TYPE).orEmpty()
        val currentDate = requireArguments().getString(ARG_DATE).orEmpty()
        val currentDesc = requireArguments().getString(ARG_DESC).orEmpty()
        val createdAt = requireArguments().getLong(ARG_CREATED_AT, Long.MIN_VALUE)
            .let { if (it == Long.MIN_VALUE) null else it }
        val latitude = requireArguments().getDouble(ARG_LATITUDE, Double.MIN_VALUE)
            .let { if (it == Double.MIN_VALUE) null else it }
        val longitude = requireArguments().getDouble(ARG_LONGITUDE, Double.MIN_VALUE)
            .let { if (it == Double.MIN_VALUE) null else it }
        val image = requireArguments().getString(ARG_IMAGE, "")
            .let { if (it == "") null else it }
        var landscape: Boolean? = requireArguments().getBoolean(ARG_IMAGE,false)
        _binding = EditableBoxesTrafficReportBinding.inflate(layoutInflater)
        binding.editTextReportTitle.setText(currentName)
        binding.editTextReportDate.setText(currentDate)
        binding.editTextReportDesc.setText(currentDesc)



        // Create a lambda for positive button click handling.
        val onPositiveButtonClick: (DialogInterface, Int) -> Unit = { dialog, _ ->
            val name = binding.editTextReportTitle.text.toString().trim()
            val userId = repository.currentUserId()
            val desc = binding.editTextReportDesc.text.toString().trim()
            val date = binding.editTextReportDate.text.toString().trim()

            if (name.isNotEmpty() && userId != null && key != null) {
                if (image==null){
                    landscape = null
                }
                repository.updateTrafficReport(
                    userId = userId,
                    key = key,
                    title = name,
                    date = date,
                    reportType = currentReportType,
                    severity = currentSeverity,
                    desc = desc,
                    createdAt = createdAt,
                    latitude = latitude,
                    longitude = longitude,
                    image = image,
                    landscape = landscape
                )
            }

        }
        return MaterialAlertDialogBuilder(requireContext()).apply {
            setView(binding.root)
            setTitle(getString(R.string.dialog_update_title))
            setMessage(getString(R.string.dialog_update_message))
            setPositiveButton(getString(R.string.button_update), onPositiveButtonClick)
            setNegativeButton(getString(R.string.button_cancel)) { dialog, _ -> dialog.dismiss() }
        }.create()
        // Create and return a new instance of MaterialAlertDialogBuilder.

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}