package com.tr.helpark.helparkcapstoneproject.core.base



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

/**
 *Created by Mert Melih Aytemur on 1/19/2024.
 */

typealias Inflater<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding,VM>(
    private val inflater : Inflater<VB>
) : Fragment() where VM : ViewModel {

    // The TAG value to use in logs.
    @Suppress("PropertyName")
    protected val TAG: String = javaClass.simpleName

    private var _binding: VB? = null

    protected val binding: VB get() = _binding as VB

    protected abstract val viewModel : VM

    protected open fun initListeners() {}

    protected open fun observeEvents() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = this.inflater.invoke(inflater,container,false)
        return binding.root
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        observeEvents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun sendPageEvent(){
        TAG.let {
            // send firebase page event
        }
    }
}