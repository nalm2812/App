package dk.itu.moapd.x9.nalm.ui.viewer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.findNavController

import androidx.core.net.toUri
import dk.itu.moapd.x9.nalm.R
import dk.itu.moapd.x9.nalm.databinding.FragmentImageBinding
import dk.itu.moapd.x9.nalm.ui.utils.viewBinding

/**
 * A fragment to display the main screen of the app.
 */
class ImageFragment : Fragment(R.layout.fragment_image) {

    /**
     * View binding is a feature that allows you to more easily write code that interacts with
     * views. Once view binding is enabled in a module, it generates a binding class for each XML
     * layout file present in that module. An instance of a binding class contains direct references
     * to all views that have an ID in the corresponding layout.
     */
    private val binding by viewBinding(FragmentImageBinding::bind)

    /**
     * Called immediately after `onCreateView(LayoutInflater, ViewGroup, Bundle)` has returned, but
     * before any saved state has been restored in to the view. This gives subclasses a chance to
     * initialize themselves once they know their view hierarchy has been completely created. The
     * fragment's view hierarchy is not however attached to its parent at this point.
     *
     * @param view The View returned by `onCreateView(LayoutInflater, ViewGroup, Bundle)`.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *      saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the listener for back button.
        binding.buttonBack.setOnClickListener {
            requireActivity().findNavController(R.id.fragment_container_view).popBackStack()
        }

        // Showing the last taken image.
        arguments?.getString("ARG_IMAGE")?.let { uri ->
            binding.imageView.setImageURI(uri.toUri())
        }
    }

}