package com.example.ownrepositarypatternsample.base

import android.content.Context
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.ownrepositarypatternsample.BR
import org.koin.androidx.scope.currentScope
import org.koin.core.qualifier.named
import kotlin.reflect.KClass

abstract class BaseActivity<VDB : ViewDataBinding, BVM : BaseViewModel>(val clazz : KClass<BVM>): AppCompatActivity() {
    protected lateinit var mBinding: VDB
    protected lateinit var mContext: Context
    protected val mViewModel: BVM by currentScope.inject(named(""), clazz, null)

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        mBinding = DataBindingUtil.setContentView(this, getLayoutId())
        mBinding.setVariable(BR.viewModel, mViewModel)
        mBinding.executePendingBindings()
        initObserve()
    }

    @LayoutRes
    abstract fun getLayoutId(): Int
    open fun initObserve() {}
}
