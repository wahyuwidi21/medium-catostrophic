package com.catastrophic.app.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.catastrophic.app.BR
import com.catastrophic.app.utils.hideKeyboard
import com.telkom.myindihomex.ui.base.MainView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel<out Any>>(
    private val toolbar: Boolean
) :
    Fragment(), CoroutineScope {

    private lateinit var job: Job
    private lateinit var dataBinding: T
    private val baseViewModel by lazy { getViewModels() }
    private lateinit var rootView: View
    private lateinit var mainView: MainView
    @LayoutRes
    abstract fun setLayout(): Int

    abstract fun getViewModels(): V

    open fun onInitialization() = Unit

    abstract fun onReadyAction()

    open fun onObserveAction() = Unit

    open fun onActivityDestroyed() = Unit

    open fun getViewDataBinding() = dataBinding

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
     }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, setLayout(), container, false)
        rootView = dataBinding.root
        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baseViewModel.toolbarState.value = toolbar
        baseViewModel.toolbarState.observe(viewLifecycleOwner) {
            if (it) {
                mainView.toolbarState(true)
            } else {
                mainView.toolbarState(false)
            }
        }
        hideKeyboard(this.requireActivity())
        dataBinding.setVariable(BR._all, baseViewModel)
        onInitialization()
        dataBinding.executePendingBindings()
        onReadyAction()
        onObserveAction()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainView = activity as MainView
    }

    override fun onDestroy() {
        super.onDestroy()
        onActivityDestroyed()
        job.cancel()
    }
}