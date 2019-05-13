package com.example.ownrepositarypatternsample.base

import android.content.Context
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.example.ownrepositarypatternsample.BR
import com.kotlinlibrary.utils.ktx.setContentBindView

abstract class BaseActivity<VDB : ViewDataBinding, BVM : BaseViewModel>: AppCompatActivity() {
    protected lateinit var mBinding: VDB
    protected abstract val mViewModel: BVM
    protected lateinit var mContext: Context

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        mBinding = setContentBindView(getLayoutId())
        mBinding.setVariable(BR.viewModel, mViewModel)
        mBinding.executePendingBindings()
        initObserve()
    }

    @LayoutRes
    abstract fun getLayoutId(): Int
    open fun initObserve() {}
}
