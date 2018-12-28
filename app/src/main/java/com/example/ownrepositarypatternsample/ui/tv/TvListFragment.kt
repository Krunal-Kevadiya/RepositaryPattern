package com.example.ownrepositarypatternsample.ui.tv

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ownrepositarypatternsample.R
import com.example.ownrepositarypatternsample.TvListBinding
import com.example.ownrepositarypatternsample.base.InjectFragment
import com.example.ownrepositarypatternsample.base.Resource
import com.example.ownrepositarypatternsample.base.Status
import com.example.ownrepositarypatternsample.data.local.entity.Tv
import com.example.ownrepositarypatternsample.ui.main.MainViewModel
import com.example.ownrepositarypatternsample.ui.tv.detail.TvDetailActivity
import com.example.ownrepositarypatternsample.utils.extension.observeLiveData
import com.example.ownrepositarypatternsample.utils.extension.startActivity
import com.example.ownrepositarypatternsample.utils.extension.toast
import com.skydoves.baserecyclerviewadapter.RecyclerViewPaginator

class TvListFragment: InjectFragment<TvListBinding, MainViewModel>(), TvListViewHolder.Delegate {
    private val adapter = TvListAdapter(this)
    private lateinit var paginator: RecyclerViewPaginator

    override fun getLayoutId(): Int = R.layout.main_fragment_tv

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
    }

    override fun initObserve() {
        observeLiveData(mViewModel.getTvListObservable()) { updateTvList(it) }
        mViewModel.postTvPage(1)
    }

    private fun initializeUI() {
        mBinding.recyclerView.adapter = adapter
        mBinding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        paginator = RecyclerViewPaginator(
                recyclerView = mBinding.recyclerView,
                isLoading = { mViewModel.getTvListValues()?.status == Status.LOADING },
                loadMore = { loadMore(it) },
                onLast = { mViewModel.getTvListValues()?.onLastPage!! }
        )
        paginator.currentPage = 1
    }

    private fun updateTvList(resource: Resource<List<Tv>>) {
        when(resource.status) {
            Status.SUCCESS -> { adapter.addTvList(resource) }
            Status.ERROR -> toast(resource.errorEnvelope?.status_message.toString())
            Status.LOADING -> { }
        }
    }

    private fun loadMore(page: Int) {
        mViewModel.postTvPage(page)
    }

    override fun onItemClick(tv: Tv) {
        startActivity<TvDetailActivity>("tv" to tv)
    }
}
