package com.example.ownrepositarypatternsample.ui.main

import android.os.Bundle
import com.example.ownrepositarypatternsample.MainBinding
import com.example.ownrepositarypatternsample.R
import com.example.ownrepositarypatternsample.base.InjectActivity
import com.example.ownrepositarypatternsample.utils.MainNavigationUtil

class MainActivity : InjectActivity<MainBinding, MainViewModel>() {
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
