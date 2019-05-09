package com.example.ownrepositarypatternsample.ui.movie

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ownrepositarypatternsample.MovieListBinding
import com.example.ownrepositarypatternsample.R
import com.example.ownrepositarypatternsample.base.Resource
import com.example.ownrepositarypatternsample.base.Status
import com.example.ownrepositarypatternsample.data.local.entity.Movie
import com.example.ownrepositarypatternsample.ui.main.MainViewModel
import com.example.ownrepositarypatternsample.ui.movie.detail.MovieDetailActivity
import com.example.ownrepositarypatternsample.utils.PaginatorList
import timber.log.Timber

class MovieListFragment : InjectFragment<MovieListBinding, MainViewModel>(), MovieListViewHolder.Delegate {
    private val adapter = MovieListAdapter(this)
    private var paginator: PaginatorList? = null

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
        mBinding.recyclerView.adapter = adapter
        mBinding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        paginator = PaginatorList(
            recyclerView = mBinding.recyclerView,
            isLoading = { mViewModel.getMovieListValues()?.status == Status.LOADING },
            currentPage = 1,
            loadMore = { loadMore(it) },
            onLast =  { mViewModel.getMovieListValues()?.onLastPage!! }
        )
    }

    private fun updateMovieList(resource: Resource<List<Movie>>) {
        when(resource.status) {
            Status.SUCCESS ->  {
                adapter.addMovieList(resource)
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
        startActivity<MovieDetailActivity>("movie" to movie)
    }
}
