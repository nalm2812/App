package dk.itu.moapd.x9.nalm.ui.utils

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentViewBindingDelegate<T : ViewBinding>(
    val fragment: Fragment,
    val viewBindingFactory: (View) -> T,
) : ReadOnlyProperty<Fragment, T> {
    /**
     * Holds the current binding instance or null if not initialized or cleared.
     */
    private var binding: T? = null

    /**
     * Initializes the delegate by observing the fragment's lifecycle to manage the binding's
     * lifecycle.
     */
    init {
        fragment.lifecycle.addObserver(
            object : DefaultLifecycleObserver {
                override fun onCreate(owner: LifecycleOwner) {
                    fragment.viewLifecycleOwnerLiveData.observe(fragment) { viewLifecycleOwner ->
                        viewLifecycleOwner.lifecycle.addObserver(
                            object : DefaultLifecycleObserver {
                                override fun onDestroy(owner: LifecycleOwner) {
                                    binding = null
                                }
                            },
                        )
                    }
                }
            },
        )
    }

    /**
     * Gets the value of the binding property, initializing it if necessary.
     *
     * @param thisRef The fragment instance.
     * @param property The property metadata.
     *
     * @return The ViewBinding instance.
     *
     * @throws IllegalStateException if the fragment's view is destroyed.
     */
    override fun getValue(
        thisRef: Fragment,
        property: KProperty<*>,
    ): T {
        val binding = binding
        if (binding != null) return binding

        val lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException(
                "Should not attempt to get bindings when Fragment views are destroyed.",
            )
        }

        return viewBindingFactory(thisRef.requireView()).also { this.binding = it }
    }
}

/**
 * Extension function to simplify the usage of [FragmentViewBindingDelegate] in fragments.
 *
 * @param T The type of ViewBinding.
 * @param factory A factory function to create the ViewBinding instance from a View.
 *
 * @return An instance of [FragmentViewBindingDelegate] for the fragment.
 */
fun <T : ViewBinding> Fragment.viewBinding(factory: (View) -> T) = FragmentViewBindingDelegate(this, factory)