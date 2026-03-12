package dk.itu.moapd.x9.nalm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel(){
    /**
     * The current text showing in the main activity.
     */
    private val _title = MutableLiveData<String>()
    /**
     * A `LiveData` which publicly exposes any update in the UI TextView.
     */
    val title: LiveData<String>
        get() = _title


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


}