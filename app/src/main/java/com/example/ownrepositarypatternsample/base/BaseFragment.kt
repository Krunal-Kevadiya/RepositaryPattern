package com.example.ownrepositarypatternsample.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.ownrepositarypatternsample.BR
import com.kotlinlibrary.utils.ktx.inflateBindView

abstract class BaseFragment<VDB : ViewDataBinding, BVM : BaseViewModel> : Fragment() {
    protected lateinit var mBinding: VDB
    protected abstract val mViewModel: BVM
    protected lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
        context?.let {
            mContext = it
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = inflateBindView(getLayoutId(), container, false)
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

    fun getBaseActivity(): BaseActivity<*, *>? {
        var mActivity: BaseActivity<*, *>? = null
        if (context is BaseActivity<*, *>) {
            val activity = context as BaseActivity<*, *>?
            mActivity = activity
        }
        return mActivity
    }

    @LayoutRes
    abstract fun getLayoutId(): Int
    open fun initObserve() {}
}
