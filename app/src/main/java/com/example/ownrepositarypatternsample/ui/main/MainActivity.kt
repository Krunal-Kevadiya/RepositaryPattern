package com.example.ownrepositarypatternsample.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.example.ownrepositarypatternsample.R
import com.example.ownrepositarypatternsample.base.BaseActivity
import com.example.ownrepositarypatternsample.data.remote.pojo.LoginPojo
import com.example.ownrepositarypatternsample.databinding.ActivityMainBinding
import com.kotlinlibrary.utils.arguments.bindArgument
import devlight.io.library.ntb.NavigationTabBar
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {
    override val mViewModel: MainViewModel by viewModel()

    private val loginDetail by bindArgument<LoginPojo>("login")

    override fun initObserve() {
        super.initObserve()
        initializeUI()
    }

    @SuppressLint("SetTextI18n")
    private fun initializeUI() {
        mBinding.mainToolbar.toolbarTitle.text = "${loginDetail.firstName} ${loginDetail.lastName}"
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
