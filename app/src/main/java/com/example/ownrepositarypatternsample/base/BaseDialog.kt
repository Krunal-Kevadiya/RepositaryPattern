package com.example.ownrepositarypatternsample.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.ownrepositarypatternsample.BR
import java.lang.reflect.ParameterizedType

abstract class BaseDialog<VDB : ViewDataBinding, BVM : BaseViewModel> : DialogFragment(), InjectFactory {
    lateinit var mBinding: VDB
    lateinit var mViewModel: BVM
    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            mContext = it
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        dialog?.let {
            it.setCancelable(false)
            it.setCanceledOnTouchOutside(false)
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = if (initFactory() != null) {
            ViewModelProviders.of(this, initFactory()).get(getViewModelClass())
        } else {
            ViewModelProviders.of(this).get(getViewModelClass())
        }
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

    private fun getViewModelClass(): Class<BVM> {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1]
        return type as Class<BVM>
    }

    @LayoutRes
    abstract fun getLayoutId(): Int
    abstract fun initObserve()

    override fun initFactory(): ViewModelProvider.Factory? = null
}
