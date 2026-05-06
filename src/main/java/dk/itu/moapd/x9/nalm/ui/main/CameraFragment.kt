package dk.itu.moapd.x9.nalm.ui.main

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.x9.nalm.R
import dk.itu.moapd.x9.nalm.camerax.CameraXController
import dk.itu.moapd.x9.nalm.databinding.FragmentCameraBinding
import dk.itu.moapd.x9.nalm.media.capture.PhotoCaptureManager
import dk.itu.moapd.x9.nalm.permission.CameraPermissionHelper
import dk.itu.moapd.x9.nalm.ui.utils.viewBinding
import kotlin.getValue

class CameraFragment: Fragment(R.layout.fragment_camera) {
    /**
     * View binding is a feature that allows you to more easily write code that interacts with
     * views. Once view binding is enabled in a module, it generates a binding class for each XML
     * layout file present in that module. An instance of a binding class contains direct references
     * to all views that have an ID in the corresponding layout.
     */
    private val binding by viewBinding(FragmentCameraBinding::bind)

    /**
     * A view model to manage the data access to the database. Using lazy initialization to create
     * the view model instance when the user access the object for the first time.
     */
    private val viewModel: MainViewModel by activityViewModels()

    /**
     * The camera selector allows to select a camera or return a filtered set of cameras.
     */
    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    /**
     * This instance provides `takePicture()` functions to take a picture to memory or save to a
     * file, and provides image metadata.
     */
    private var imageCapture: ImageCapture? = null

    /**
     * This object launches a new permission dialog and receives back the user's permission.
     */
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) startCamera() else requireActivity().finish()
    }

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

        // Observe imageUri from the ViewModel so the fragment UI reflects the latest saved photo.
        viewModel.imageUri.observe(viewLifecycleOwner) { uri ->
            // Update the local UI state if needed. Keep a small local cache so the click
            // listener below can access it without directly reading LiveData each time.
            imageUriLocal = uri
        }

        // Request camera permissions.
        if (CameraPermissionHelper.hasCameraPermission(requireContext())) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        // The current selected camera.
        viewModel.selector.observe(viewLifecycleOwner) {
            // Only update the local selector when ViewModel provides a non-null value.
            // This avoids resetting to DEFAULT_BACK_CAMERA on configuration change
            // when LiveData doesn't have a value yet.
            cameraSelector = it ?: cameraSelector
        }

        // Define the UI behavior.
        binding.apply {

            // Set up the listener for take photo button.
            buttonImageCapture.setOnClickListener {
                takePhoto()
            }

            // Set up the listener for the change camera button.
            buttonCameraSwitch.apply {

                // Disable the button until the camera is set up
                isEnabled = false

                setOnClickListener {
                    viewModel.onCameraSelectorChanged(
                        if (cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA)
                            CameraSelector.DEFAULT_BACK_CAMERA
                        else
                            CameraSelector.DEFAULT_FRONT_CAMERA
                    )

                    // Re-start use cases to update selected camera.
                    startCamera()
                }
            }

            // Set up the listener for the photo view button.
            buttonImageViewer.setOnClickListener {
                imageUriLocal?.let { uri ->
                    requireActivity().findNavController(R.id.fragment_container_view)
                        .navigate(R.id.action_camera_to_image, bundleOf("ARG_IMAGE" to uri.toString()))
                }
            }
        }
    }

    // Small local cache for the observed imageUri so the click listener can reference it.
    private var imageUriLocal: Uri? = null

    /**
     * This method is used to start the video camera device stream.
     */
    private fun startCamera() {
        // Ensure we use the latest selector from the ViewModel (if available)
        // before starting/binding CameraX. This preserves the user's choice
        // across configuration changes (rotation).
        cameraSelector = viewModel.selector.value ?: cameraSelector

        CameraXController.startCamera(
            fragment = this,
            selector = cameraSelector,
            viewFinder = binding.viewFinder,
            onImageCaptureReady = { imageCapture = it },
            onCanSwitchCamera = { binding.buttonCameraSwitch.isEnabled = it },
            onError = ::showSnackBar,
        )
    }

    /**
     * This method is used to save a frame from the video camera device stream as a JPG photo.
     */
    private fun takePhoto() {
        val capture = imageCapture ?: return
        PhotoCaptureManager.takePhoto(
            context = requireContext(),
            contentResolver = requireActivity().contentResolver,
            imageCapture = capture,
            onSaved = { uri, filename ->
                // Persist the captured Uri into the ViewModel so it survives rotation.
                viewModel.onImageUriChanged(uri)
                showSnackBar("Photo capture succeeded: $filename")
            },
            onError = { message, _ ->
                showSnackBar("Photo capture failed: $message")
            },
        )
    }

    /**
     * Displays a SnackBar to show a brief message about the clicked button.
     *
     * The SnackBar is created using the clicked button's information and is shown at the bottom of
     * the screen.
     *
     * @param message The message to be displayed in the SnackBar.
     */
    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.root, message, Snackbar.LENGTH_SHORT
        ).show()
    }
}