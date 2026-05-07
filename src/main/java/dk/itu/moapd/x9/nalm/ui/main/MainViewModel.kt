package dk.itu.moapd.x9.nalm.ui.main

import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ValueEventListener
import dk.itu.moapd.x9.nalm.ui.main.MainUiState
import dk.itu.moapd.x9.nalm.data.repository.TrafficReportRepository
import dk.itu.moapd.x9.nalm.domain.model.TrafficReportModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel(){


    /**
    * The current selected camera.
    */
    private var _selector = MutableLiveData<CameraSelector>()

    /**
     * A `LiveData` which publicly exposes any update in the camera selector.
     */
    val selector: LiveData<CameraSelector>
        get() = _selector

    /**
     * The last captured image Uri.
     */
    private var _imageUri = MutableLiveData<Uri?>()

    /**
     * A `LiveData` which publicly exposes any update in the last captured image Uri.
     */
    val imageUri: LiveData<Uri?>
        get() = _imageUri

    private var _filename = MutableLiveData<String?>()

    val filename: LiveData<String?>
        get() = _filename

    /**
     * This method will be executed when the user interacts with the camera selector component. It
     * sets the selector into the LiveData instance.
     *
     * @param selector A set of requirements and priorities used to select a camera.
     */
    fun onCameraSelectorChanged(selector: CameraSelector) {
        this._selector.value = selector
    }

    /**
     * Update the last captured image Uri.
     *
     * @param uri The new image Uri.
     */
    fun onImageUriChanged(uri: Uri?) {
        _imageUri.value = uri
    }

    fun onFilenameChanged(filename: String) {
        _filename.value = filename
    }



    /**
     * The current text showing in the main activity.
     */
    private val _title = MutableLiveData<String>()
    /**
     * A `LiveData` which publicly exposes any update in the UI TextView.
     */
    val title: LiveData<String>
        get() = _title
    private val repository by lazy { TrafficReportRepository() }
    private var listener: ValueEventListener? = null



    /**
     * This method will be executed when the user interacts with any UI component and it is
     * necessary to update the text in the UI TextView. It sets the text into the LiveData instance.
     *
     * @param text A `String` to show in the UI TextView.
     */
    fun setTitle(text: String) {
        _title.value = text
    }
    private val _items = MutableLiveData<List<TrafficReportModel>>(emptyList())

    val items: LiveData<List<TrafficReportModel>> = _items
    fun addItem(item: TrafficReportModel) {
        val current = _items.value.orEmpty().toMutableList()
        current.add(item)
        _items.value = current
    }

    fun setItems(list: List<TrafficReportModel>) {
        _items.value = list
    }


    private fun createTrafficReport() : List<TrafficReportModel> =
        (1..3).map { index ->
            TrafficReportModel(
                title = "title",
                location = "location",
                date = "date",
                reportType = "report type",
                severity = "severity",
                desc = "description"
            )

        }
    private val _uiState = MutableStateFlow(MainUiState())

    /**
     * A `StateFlow` which publicly exposes any update in the UI components.
     */
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    /**
     * Updates the text to be displayed based on the selected text resource ID.
     *
     * @param textId The resource ID of the text to be displayed.
     */
    fun onReportTypeSelected(reportType: String) {
        _uiState.update { it.copy(reportType = reportType) }
    }
    fun onSeveritySelected(severity: String) {
        _uiState.update { it.copy(severity = severity) }
    }



    fun updateReportList(){
        val userId = repository.currentUserId() ?: return

        val query = repository.trafficReportQuery(userId)
        query.get()
            .addOnSuccessListener { list ->
                for (child in list.children) {
                    Log.v("testing", "do we get here?")
                    val report = child.getValue(TrafficReportModel::class.java)
                    if (report != null) {
                        Log.v("testing", "title: " + report.title)
                        addItem(report)
                    }
                }
            }
            .addOnFailureListener { error ->
                Log.e("myTag", "Error: ${error.message}")
            }


    }
}