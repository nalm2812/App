package dk.itu.moapd.x9.nalm.ui.main

import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dk.itu.moapd.x9.nalm.data.repository.TrafficReportRepository
import dk.itu.moapd.x9.nalm.domain.model.TrafficReportModel

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

    private var _isLandscape = MutableLiveData<Boolean?>()

    val isLandscape: LiveData<Boolean?>
        get() = _isLandscape

    private val _latitude = MutableLiveData<Double?>()
    val latitude : LiveData<Double?>
        get() = _latitude
    private val _longitude = MutableLiveData<Double?>()
    val longitude : LiveData<Double?>
        get() = _longitude

    fun setLatitude(latitude: Double?) {
        _latitude.value = latitude
    }
    fun setLongitude(longitude: Double?) {
        _longitude.value = longitude
    }
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

    fun onFilenameChanged(filename: String?) {
        _filename.value = filename
    }

    fun onLandscapeChanged(landscape: Boolean?){
        _isLandscape.value = landscape
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








    fun updateReportList(){
        val userId = repository.currentUserId() ?: return
        val query = repository.trafficReportQuery(userId)
        query.get()
            .addOnSuccessListener { list ->
                for (idk in list.children) {
                    for (child in idk.children){
                        val report = child.getValue(TrafficReportModel::class.java)
                        if (report != null) {
                            addItem(report)
                        }
                    }

                }
            }
            .addOnFailureListener { error ->
                Log.e("myTag", "Error: ${error.message}")
            }


    }
}