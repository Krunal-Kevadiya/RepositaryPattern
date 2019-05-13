package com.example.ownrepositarypatternsample.ui.main

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.example.ownrepositarypatternsample.R
import com.example.ownrepositarypatternsample.base.BaseActivity
import com.example.ownrepositarypatternsample.databinding.ActivityMainBinding
import com.example.ownrepositarypatternsample.utils.extension.currentScope
import devlight.io.library.ntb.NavigationTabBar

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
        setComponents(this, mBinding.mainViewpager, mBinding.mainBottomNavigation)
    }

    private fun setComponents(context: Context, viewPager: ViewPager, navigationTabBar: NavigationTabBar) {
        val colors = context.resources.getStringArray(R.array.default_preview)
        val models = ArrayList<NavigationTabBar.Model>()
        models.add(
            NavigationTabBar.Model.Builder(
                ContextCompat.getDrawable(context, R.drawable.ic_movie_filter_white_24dp),
                Color.parseColor(colors[0]))
                .title(context.getString(R.string.menu_movie))
                .build()
        )
        models.add(
            NavigationTabBar.Model.Builder(
                ContextCompat.getDrawable(context, R.drawable.ic_live_tv_white_24dp),
                Color.parseColor(colors[1]))
                .title(context.getString(R.string.menu_tv))
                .build()
        )
        models.add(
            NavigationTabBar.Model.Builder(
                ContextCompat.getDrawable(context, R.drawable.ic_star_white_24dp),
                Color.parseColor(colors[2]))
                .title(context.getString(R.string.menu_star))
                .build()
        )
        navigationTabBar.models = models
        navigationTabBar.setViewPager(viewPager, 0)
    }
}
