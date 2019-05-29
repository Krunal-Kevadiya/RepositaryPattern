package com.example.ownrepositarypatternsample.base

import android.content.Context
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.example.ownrepositarypatternsample.BR
import com.kotlinlibrary.snackbar.Snackbar
import com.kotlinlibrary.snackbar.util.SnackBatType
import com.kotlinlibrary.snackbar.util.snackBarMessage
import com.kotlinlibrary.statusbaralert.StatusBarAlert
import com.kotlinlibrary.statusbaralert.StatusBarAlertView
import com.kotlinlibrary.statusbaralert.progressMessage
import com.kotlinlibrary.utils.ktx.observeLiveData
import com.kotlinlibrary.utils.ktx.setContentBindView
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

abstract class BaseActivity<VDB : ViewDataBinding, BVM : BaseViewModel>(
    @LayoutRes val layoutRes: Int
): AppCompatActivity() {
    protected lateinit var mBinding: VDB
    protected lateinit var mContext: Context
    protected val mViewModel: BVM by viewModel(viewModelClass())

    private lateinit var snackBarMsg: Snackbar
    private var statusBarProgress: StatusBarAlertView? = null

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        mBinding = setContentBindView(layoutRes)
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

    open fun initObserve() {}

    @Suppress("UNCHECKED_CAST")
    private fun viewModelClass(): KClass<BVM> {
        return ((javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<BVM>).kotlin
    }

    protected fun showAlertView(isShow: Boolean) {
        if (isShow) {
            if (statusBarProgress != null) {
                StatusBarAlert.hide(this@BaseActivity, Runnable {})
            }
            statusBarProgress = progressMessage(msg = "Please wait")
            statusBarProgress?.showIndeterminateProgress()
        } else {
            StatusBarAlert.hide(this@BaseActivity, Runnable {})
        }
    }

    override fun onPause() {
        if (::snackBarMsg.isInitialized)
            snackBarMsg.dismiss()
        showAlertView(false)
        super.onPause()
    }
}
