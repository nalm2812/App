package dk.itu.moapd.x9.nalm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainViewModel {
    /**
     * The current text showing in the main activity.
     */
    private val _message = MutableLiveData<String>()
    /**
     * A `LiveData` which publicly exposes any update in the UI TextView.
     */
    val message: LiveData<String>
        get() = _message
    /**
     * This method will be executed when the user interacts with any UI component and it is
     * necessary to update the text in the UI TextView. It sets the text into the LiveData instance.
     *
     * @param text A `String` to show in the UI TextView.
     */
    fun setMessage(text: String) {
        _message.value = text
    }
}