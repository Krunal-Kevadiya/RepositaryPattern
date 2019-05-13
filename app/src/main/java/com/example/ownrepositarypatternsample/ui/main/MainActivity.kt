package com.example.ownrepositarypatternsample.ui.main

import android.os.Bundle
import com.example.ownrepositarypatternsample.R
import com.example.ownrepositarypatternsample.base.BaseActivity
import com.example.ownrepositarypatternsample.databinding.ActivityMainBinding
import com.example.ownrepositarypatternsample.utils.MainNavigationUtil
import com.example.ownrepositarypatternsample.utils.extension.currentScope

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    override val mViewModel: MainViewModel by currentScope<MainActivity>().inject()

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initializeUI()
    }

    private fun initializeUI() {
        mBinding.mainViewpager.adapter = MainPagerAdapter(supportFragmentManager)
        mBinding.mainViewpager.offscreenPageLimit = 3
        MainNavigationUtil.setComponents(this, mBinding.mainViewpager, mBinding.mainBottomNavigation)
    }
}
