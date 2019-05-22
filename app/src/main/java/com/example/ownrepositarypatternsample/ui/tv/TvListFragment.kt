package com.example.ownrepositarypatternsample.ui.tv

import android.app.Activity
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.ownrepositarypatternsample.BR
import com.example.ownrepositarypatternsample.R
import com.example.ownrepositarypatternsample.base.BaseFragment
import com.example.ownrepositarypatternsample.base.repository.ScreenState
import com.example.ownrepositarypatternsample.data.Api
import com.example.ownrepositarypatternsample.data.local.entity.Tv
import com.example.ownrepositarypatternsample.databinding.ItemTvBinding
import com.example.ownrepositarypatternsample.databinding.MainFragmentTvBinding
import com.example.ownrepositarypatternsample.ui.main.MainViewModel
import com.example.ownrepositarypatternsample.ui.tv.detail.TvDetailActivity
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
import org.koin.androidx.viewmodel.ext.android.viewModel

class TvListFragment: BaseFragment<MainFragmentTvBinding, MainViewModel>(R.layout.main_fragment_tv) {
    override val mViewModel: MainViewModel by viewModel()

    private var noPaginate: NoPaginate? = null
    private var adapter: SingleBindingAdapter<Tv>? = null

    override fun initObserve() {
        observeLiveData(mViewModel.getTvListObservable()) { updateTvList(it) }

        initializeUI()
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
        noPaginate?.unbind()
        super.onDestroy()
    }

    private fun updateTvList(resource: ScreenState<List<Tv>>) {
        when(resource) {
            is ScreenState.LoadingState.Show -> {
                if(resource.isInitial) {
                    showAlertView(true)
                } else {
                    noPaginate?.showError(false)
                    noPaginate?.showLoading(true)
                }
            }
            is ScreenState.LoadingState.Hide -> {
                if(resource.isInitial) {
                    showAlertView(false)
                } else {
                    noPaginate?.showLoading(false)
                }
            }
            is ScreenState.SuccessState.Api -> {
                noPaginate?.setNoMoreItems(resource.onLastPage)
                resource.data?.let {
                    val list = adapter?.getItemLists()?.toMutableList() ?: mutableListOf()
                    list.addAll(it.toMutableList())
                    adapter?.reSet(list.distinct().toMutableList())
                }
            }
            is ScreenState.ErrorState.Api -> {
                noPaginate?.showError(true)
                mViewModel.message.postValue(resource.message)
            }
        }
    }
}
