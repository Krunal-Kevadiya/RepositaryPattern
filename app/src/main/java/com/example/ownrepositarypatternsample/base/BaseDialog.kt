package com.example.ownrepositarypatternsample.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.example.ownrepositarypatternsample.BR
import com.kotlinlibrary.utils.ktx.inflateBindView
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

abstract class BaseDialog<VDB : ViewDataBinding, BVM : BaseViewModel>(
    @LayoutRes val layoutRes: Int
) : DialogFragment() {
    protected lateinit var mBinding: VDB
    protected lateinit var mContext: Context
    protected val mViewModel: BVM by viewModel(viewModelClass())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            mContext = it
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = inflateBindView(layoutRes, container, false)
        dialog?.let {
            it.setCancelable(false)
            it.setCanceledOnTouchOutside(false)
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.setVariable(BR.viewModel, mViewModel)
        mBinding.executePendingBindings()
        initObserve()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let { dialogWindow ->
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialogWindow.setLayout(width, height)
            dialogWindow.setBackgroundDrawableResource(android.R.color.transparent)
        }
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
}
