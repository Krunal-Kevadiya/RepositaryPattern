package com.example.ownrepositarypatternsample.ui.tv

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.ownrepositarypatternsample.BR
import com.example.ownrepositarypatternsample.R
import com.example.ownrepositarypatternsample.base.BaseFragment
import com.example.ownrepositarypatternsample.base.Resource
import com.example.ownrepositarypatternsample.base.Status
import com.example.ownrepositarypatternsample.data.Api
import com.example.ownrepositarypatternsample.data.local.entity.Tv
import com.example.ownrepositarypatternsample.databinding.ItemTvBinding
import com.example.ownrepositarypatternsample.databinding.MainFragmentTvBinding
import com.example.ownrepositarypatternsample.ui.main.MainActivity
import com.example.ownrepositarypatternsample.ui.main.MainViewModel
import com.example.ownrepositarypatternsample.ui.tv.detail.TvDetailActivity
import com.example.ownrepositarypatternsample.utils.extension.currentScope
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette
import com.kotlinlibrary.loadmore.item.ErrorItem
import com.kotlinlibrary.loadmore.item.LoadingItem
import com.kotlinlibrary.loadmore.paginate.Direction
import com.kotlinlibrary.loadmore.paginate.NoPaginate
import com.kotlinlibrary.recycleradapter.setUpBinding
import com.kotlinlibrary.recycleradapter.simple.SingleBindingAdapter
import com.kotlinlibrary.utils.ktx.observeLiveData
import com.kotlinlibrary.utils.navigate.launchActivity
import org.jetbrains.anko.toast
import timber.log.Timber

class TvListFragment: BaseFragment<MainFragmentTvBinding, MainViewModel>() {
    override val mViewModel: MainViewModel by currentScope<MainActivity>().inject()
    private var adapter: SingleBindingAdapter<Tv>? = null
    private lateinit var noPaginate: NoPaginate

    override fun getLayoutId(): Int = R.layout.main_fragment_tv

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
    }

    override fun initObserve() {
        observeLiveData(mViewModel.getTvListObservable()) { updateTvList(it) }
    }

    private fun initializeUI() {
        adapter = mBinding.recyclerView.setUpBinding<Tv> {
            withLayoutManager(GridLayoutManager(context, 2))
            withLayoutResId(R.layout.item_tv)
            onBind<ItemTvBinding>(BR.data) { _, item ->
                item.posterPath?.let {
                    Glide.with(mContext)
                        .load(Api.getPosterPath(it))
                        .listener(
                            GlidePalette.with(Api.getPosterPath(it))
                            .use(BitmapPalette.Profile.VIBRANT)
                            .intoBackground(itemPosterPalette)
                            .crossfade(true))
                        .into(itemPosterPost)
                }
            }
            onClick { _, _, item ->
                (mContext as Activity).launchActivity<TvDetailActivity>(params = *arrayOf("tv" to item))
            }
            withItems(mutableListOf())
        }
        setupLoadMore()
    }

    private fun setupLoadMore() {
        noPaginate = NoPaginate {
            loadingTriggerThreshold = 0
            recyclerView = mBinding.recyclerView
            loadingItem = LoadingItem.DEFAULT
            errorItem = ErrorItem.DEFAULT
            direction = Direction.DOWN
            onLoadMore = {
                mViewModel.postTvPage()
            }
        }
    }

    override fun onDestroy() {
        noPaginate.unbind()
        super.onDestroy()
    }

    private fun updateTvList(resource: Resource<List<Tv>>) {
        when(resource.status) {
            Status.SUCCESS ->  {
                Timber.e("Load Tv List ${resource.data}")
                noPaginate.showLoading(false)
                noPaginate.setNoMoreItems(mViewModel.getTvListValues()?.onLastPage!!)
                resource.data?.let {
                    adapter?.addAll(it.toMutableList())
                }
            }
            Status.ERROR -> {
                Timber.e("Error Tv List")
                mContext.toast(resource.message.toString())
                noPaginate.showLoading(false)
                noPaginate.showError(true)
                noPaginate.setNoMoreItems(mViewModel.getTvListValues()?.onLastPage!!)
            }
            Status.LOADING -> {
                Timber.e("Loading Tv List")
                noPaginate.showError(false)
                noPaginate.showLoading(true)
            }
        }
    }
}
