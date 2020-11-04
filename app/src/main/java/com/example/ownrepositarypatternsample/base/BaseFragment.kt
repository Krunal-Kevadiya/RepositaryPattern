package com.example.ownrepositarypatternsample.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.ownrepositarypatternsample.BR
import com.kotlinlibrary.snackbar.Snackbar
import com.kotlinlibrary.snackbar.util.SnackBatType
import com.kotlinlibrary.snackbar.util.snackBarMessage
import com.kotlinlibrary.statusbaralert.StatusBarAlert
import com.kotlinlibrary.statusbaralert.StatusBarAlertView
import com.kotlinlibrary.statusbaralert.progressMessage
import com.kotlinlibrary.utils.ktx.inflateBindView
import com.kotlinlibrary.utils.ktx.logs
import com.kotlinlibrary.utils.ktx.observeLiveData
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

abstract class BaseFragment<VDB : ViewDataBinding, BVM : BaseViewModel>(
    @LayoutRes val layoutRes: Int
) : Fragment() {
    protected lateinit var mBinding: VDB
    protected lateinit var mContext: Context
    protected val mViewModel: BVM by viewModel(viewModelClass())

    private lateinit var snackBarMsg: Snackbar
    private var statusBarProgress: StatusBarAlertView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
        context?.let {
            mContext = it
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = inflateBindView(layoutRes, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.setVariable(BR.viewModel, mViewModel)
        mBinding.executePendingBindings()

        observeLiveData(mViewModel.message) { msg ->
            msg?.let {
                snackBarMsg = mContext.snackBarMessage(SnackBatType.INFO, msg = it).build()
                snackBarMsg.show()
            }
        }
        initObserve()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    fun getBaseActivity(): BaseActivity<*, *>? {
        var mActivity: BaseActivity<*, *>? = null
        if (context is BaseActivity<*, *>) {
            val activity = context as BaseActivity<*, *>?
            mActivity = activity
        }
        return mActivity
    }

    open fun initObserve() {}

    @Suppress("UNCHECKED_CAST")
    private fun viewModelClass(): KClass<BVM> {
        return ((javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<BVM>).kotlin
    }

    protected fun showAlertView(isShow: Boolean) {
        if(isShow) {
            if (statusBarProgress != null) {
                StatusBarAlert.hide(mContext as Activity, Runnable {})
            }
            statusBarProgress = (mContext as Activity).progressMessage(msg = "Please wait")
            statusBarProgress?.showIndeterminateProgress()
        } else {
            StatusBarAlert.hide(mContext as Activity, Runnable {})
        }
    }

    override fun onPause() {
        if (::snackBarMsg.isInitialized)
            snackBarMsg.dismiss()
        showAlertView(false)
        super.onPause()
    }
}
