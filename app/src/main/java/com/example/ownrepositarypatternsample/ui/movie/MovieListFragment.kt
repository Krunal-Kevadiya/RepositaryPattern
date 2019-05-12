package com.example.ownrepositarypatternsample.ui.movie

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ownrepositarypatternsample.MovieListBinding
import com.example.ownrepositarypatternsample.R
import com.example.ownrepositarypatternsample.base.BaseFragment
import com.example.ownrepositarypatternsample.base.Resource
import com.example.ownrepositarypatternsample.base.Status
import com.example.ownrepositarypatternsample.data.local.entity.Movie
import com.example.ownrepositarypatternsample.ui.main.MainActivity
import com.example.ownrepositarypatternsample.ui.main.MainViewModel
import com.example.ownrepositarypatternsample.ui.movie.detail.MovieDetailActivity
import com.example.ownrepositarypatternsample.utils.PaginatorList
import com.example.ownrepositarypatternsample.utils.extension.currentScope
import com.example.ownrepositarypatternsample.utils.extension.observeLiveData
import com.example.ownrepositarypatternsample.utils.extension.startActivitys
import com.example.ownrepositarypatternsample.utils.extension.toast
import com.kotlinlibrary.loadmore.item.ErrorItem
import com.kotlinlibrary.loadmore.item.LoadingItem
import com.kotlinlibrary.loadmore.paginate.Direction
import com.kotlinlibrary.loadmore.paginate.NoPaginate
import com.kotlinlibrary.recycleradapter.setUpBinding
import com.kotlinlibrary.recycleradapter.simple.SingleBindingAdapter
import timber.log.Timber

class MovieListFragment : BaseFragment<MovieListBinding, MainViewModel>(), MovieListViewHolder.Delegate {
    override val mViewModel: MainViewModel by currentScope<MainActivity>().inject()
    private var adapter: SingleBindingAdapter<Movie>? = null
    private lateinit var noPaginate: NoPaginate

    override fun getLayoutId(): Int = R.layout.main_fragment_movie

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
    }

    override fun initObserve() {
        observeLiveData(mViewModel.getMovieListObservable()) {
            updateMovieList(it)
        }
        mViewModel.postMoviePage(1)
    }

    private fun initializeUI() {
        adapter = mBinding.recyclerView.setUpBinding<Movie> {
            withLayoutManager(GridLayoutManager(context, 2))
            withLayoutResId(R.layout.item_poster)
            onBind { _, _ ->
            }
            onClick { id, index, item ->

            }
            withItems(mutableListOf())
        }
        paginator = PaginatorList(
            recyclerView = mBinding.recyclerView,
            isLoading = { mViewModel.getMovieListValues()?.status == Status.LOADING },
            currentPage = 1,
            loadMore = { loadMore(it) },
            onLast =  { mViewModel.getMovieListValues()?.onLastPage!! }
        )
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
                mViewModel.getMovieListValues()?.status == Status.LOADING
                noPaginate.showError(false)
                noPaginate.showLoading(true)
                loadMore(it)

                Handler(Looper.getMainLooper()).postDelayed({
                    if (Random(25).nextInt() % 2 == 0) {
                        count++
                        noPaginate.showLoading(false)
                        noPaginate.setNoMoreItems(count > 3)
                        recyclerView.post {
                            val list = MutableList(10) { index -> "LoadMore -> ${index + (adapter.itemCount + 1)}" }
                            adapter + list
                        }
                    } else {
                        noPaginate.showLoading(false)
                        noPaginate.showError(true)
                    }
                }, 5000)
            }
        }
    }

    override fun onDestroy() {
        noPaginate.unbind()
        super.onDestroy()
    }

    private fun updateMovieList(resource: Resource<List<Movie>>) {
        when(resource.status) {
            Status.SUCCESS ->  {
                resource.data?.let {
                    adapter?.addAll(it.toMutableList())
                }
                paginator?.initFirstTime = false
            }
            Status.ERROR -> toast(resource.errorEnvelope?.status_message.toString())
            Status.LOADING -> {
                Timber.e("Loading Movie List")
            }
        }
    }

    private fun loadMore(page: Int) {
        mViewModel.postMoviePage(page)
    }

    override fun onItemClick(movie: Movie) {
        startActivitys<MovieDetailActivity>("movie" to movie)
    }
}
