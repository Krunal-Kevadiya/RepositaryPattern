package com.example.ownrepositarypatternsample.ui.movie.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ownrepositarypatternsample.MovieDetailBinding
import com.example.ownrepositarypatternsample.R
import com.example.ownrepositarypatternsample.base.BaseActivity
import com.example.ownrepositarypatternsample.base.Resource
import com.example.ownrepositarypatternsample.base.Status
import com.example.ownrepositarypatternsample.data.Api
import com.example.ownrepositarypatternsample.data.local.entity.Movie
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Keyword
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Review
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Video
import com.example.ownrepositarypatternsample.utils.KeywordListMapper
import com.example.ownrepositarypatternsample.utils.extension.*
import org.jetbrains.anko.toast

class MovieDetailActivity : BaseActivity<MovieDetailBinding, MovieDetailViewModel>(), VideoListViewHolder.Delegate {
    override val mViewModel: MovieDetailViewModel by currentScope<MovieDetailActivity>().inject()
    private val videoAdapter by lazy { VideoListAdapter(this) }
    private val reviewAdapter by lazy { ReviewListAdapter() }

    override fun getLayoutId(): Int = R.layout.activity_movie_detail

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initializeUI()
    }

    override fun initObserve() {
        observeLiveData(mViewModel.getKeywordListObservable()) { updateKeywordList(it) }
        mViewModel.postKeywordId(getMovieFromIntent().id)

        observeLiveData(mViewModel.getVideoListObservable()) { updateVideoList(it) }
        mViewModel.postVideoId(getMovieFromIntent().id)

        observeLiveData(mViewModel.getReviewListObservable()) { updateReviewList(it) }
        mViewModel.postReviewId(getMovieFromIntent().id)
    }

    private fun initializeUI() {
        applyToolbarMargin(mBinding.movieDetailToolbar)
        simpleToolbarWithHome(mBinding.movieDetailToolbar, getMovieFromIntent().title)
        getMovieFromIntent().backdrop_path?.let {
            Glide.with(this).load(Api.getBackdropPath(it))
                    .listener(requestGlideListener(mBinding.movieDetailPoster))
                    .into(mBinding.movieDetailPoster)
        } ?: let {
            Glide.with(this).load(Api.getBackdropPath(getMovieFromIntent().poster_path!!))
                    .listener(requestGlideListener(mBinding.movieDetailPoster))
                    .into(mBinding.movieDetailPoster)
        }
        mBinding.incHeader.detailHeaderTitle.text = getMovieFromIntent().title
        mBinding.incHeader.detailHeaderRelease.text = "Release Date : ${getMovieFromIntent().release_date}"
        mBinding.incHeader.detailHeaderStar.rating = getMovieFromIntent().vote_average / 2
        mBinding.incBody.detailBodyRecyclerViewTrailers.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mBinding.incBody.detailBodyRecyclerViewTrailers.adapter = videoAdapter
        mBinding.incBody.detailBodySummary.text = getMovieFromIntent().overview
        mBinding.incBody.detailBodyRecyclerViewReviews.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding.incBody.detailBodyRecyclerViewReviews.adapter = reviewAdapter
        mBinding.incBody.detailBodyRecyclerViewReviews.isNestedScrollingEnabled = false
        mBinding.incBody.detailBodyRecyclerViewReviews.setHasFixedSize(true)
    }

    private fun updateKeywordList(resource: Resource<List<Keyword>>) {
        when(resource.status) {
            Status.SUCCESS -> {
                mBinding.incBody.detailBodyTags.tags = KeywordListMapper.mapToStringList(resource.data!!)

                if(resource.data.isNotEmpty()) {
                    mBinding.incBody.detailBodyTags.visible()
                }
            }
            Status.ERROR -> toast(resource.errorEnvelope?.status_message.toString())
            Status.LOADING -> { }
        }
    }

    private fun updateVideoList(resource: Resource<List<Video>>) {
        when(resource.status) {
            Status.SUCCESS -> {
                videoAdapter.addVideoList(resource)

                if(resource.data?.isNotEmpty()!!) {
                    mBinding.incBody.detailBodyTrailers.visible()
                    mBinding.incBody.detailBodyRecyclerViewTrailers.visible()
                }
            }
            Status.ERROR -> toast(resource.errorEnvelope?.status_message.toString())
            Status.LOADING -> { }
        }
    }

    private fun updateReviewList(resource: Resource<List<Review>>) {
        when(resource.status) {
            Status.SUCCESS -> {
                reviewAdapter.addReviewList(resource)

                if(resource.data?.isNotEmpty()!!) {
                    mBinding.incBody.detailBodyReviews.visible()
                    mBinding.incBody.detailBodyRecyclerViewReviews.visible()
                }
            }
            Status.ERROR -> toast(resource.errorEnvelope?.status_message.toString())
            Status.LOADING -> { }
        }
    }

    private fun getMovieFromIntent(): Movie {
        return intent.getParcelableExtra("movie") as Movie
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == android.R.id.home) onBackPressed()
        return false
    }

    override fun onItemClicked(video: Video) {
        val playVideoIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Api.getYoutubeVideoPath(video.key)))
        startActivity(playVideoIntent)
    }
}
