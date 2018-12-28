package com.example.ownrepositarypatternsample.base

import android.content.Context
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.ownrepositarypatternsample.BR
import dagger.Provides
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<VDB : ViewDataBinding, BVM : BaseViewModel> : AppCompatActivity(), InjectFactory {
    lateinit var mBinding: VDB
    lateinit var mViewModel: BVM
    lateinit var mContext: Context

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        mBinding = DataBindingUtil.setContentView(this, getLayoutId())
        mViewModel = if (initFactory() != null) {
            ViewModelProviders.of(this, initFactory()).get(getViewModelClass())
        } else {
            ViewModelProviders.of(this).get(getViewModelClass())
        }
        mBinding.setVariable(BR.viewModel, mViewModel)
        mBinding.executePendingBindings()
        initObserve()
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
