package com.example.ownrepositarypatternsample.ui.main

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.example.ownrepositarypatternsample.MainBinding
import com.example.ownrepositarypatternsample.R
import com.example.ownrepositarypatternsample.base.BaseActivity
import com.example.ownrepositarypatternsample.utils.MainNavigationUtil
import org.koin.androidx.scope.currentScope
import kotlin.reflect.KClass

class MainActivity : BaseActivity<MainBinding, MainViewModel>() {
    override var mViewModel: ViewModel by currentScope.get(KClass<MainViewModel::class>)

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initObserve() {

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initializeUI()
    }

    private fun initializeUI() {
        mBinding.mainViewpager.adapter = MainPagerAdapter(supportFragmentManager)
        //mBinding.mainViewpager.offscreenPageLimit = 3
        MainNavigationUtil.setComponents(this, mBinding.mainViewpager, mBinding.mainBottomNavigation)
    }
}
