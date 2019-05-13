package com.example.ownrepositarypatternsample.ui.movie.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.example.ownrepositarypatternsample.BR
import com.example.ownrepositarypatternsample.R
import com.example.ownrepositarypatternsample.base.BaseActivity
import com.example.ownrepositarypatternsample.base.Resource
import com.example.ownrepositarypatternsample.base.Status
import com.example.ownrepositarypatternsample.data.Api
import com.example.ownrepositarypatternsample.data.local.entity.Movie
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Keyword
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Review
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Video
import com.example.ownrepositarypatternsample.databinding.ActivityMovieDetailBinding
import com.example.ownrepositarypatternsample.databinding.ItemReviewBinding
import com.example.ownrepositarypatternsample.databinding.ItemVideoBinding
import com.example.ownrepositarypatternsample.utils.KeywordListMapper
import com.example.ownrepositarypatternsample.utils.extension.*
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette
import com.kotlinlibrary.recycleradapter.setUpBinding
import com.kotlinlibrary.recycleradapter.simple.SingleBindingAdapter
import com.kotlinlibrary.utils.ktx.applyToolbarMargin
import com.kotlinlibrary.utils.ktx.observeLiveData
import com.kotlinlibrary.utils.ktx.simpleToolbarWithHome
import com.kotlinlibrary.utils.ktx.visible
import org.jetbrains.anko.toast

class MovieDetailActivity : BaseActivity<ActivityMovieDetailBinding, MovieDetailViewModel>() {
    override val mViewModel: MovieDetailViewModel by currentScope<MovieDetailActivity>().inject()
    private var videoAdapter: SingleBindingAdapter<Video>? = null
    private var reviewAdapter: SingleBindingAdapter<Review>? = null

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
        mBinding.incBody.detailBodySummary.text = getMovieFromIntent().overview

        videoAdapter = mBinding.incBody.detailBodyRecyclerViewTrailers.setUpBinding<Video> {
            withLayoutResId(R.layout.item_video)
            onBind<ItemVideoBinding>(BR.data) { _, item ->
                item.key?.let {
                    Glide.with(this@MovieDetailActivity)
                        .load(Api.getYoutubeThumbnailPath(it))
                        .listener(
                            GlidePalette.with(Api.getYoutubeThumbnailPath(it))
                            .use(BitmapPalette.Profile.VIBRANT)
                            .intoBackground(itemVideoPalette)
                            .crossfade(true))
                        .into(itemVideoCover)
                }
            }
            onClick { _, _, item ->
                item.key?.let {
                    val playVideoIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Api.getYoutubeVideoPath(it)))
                    startActivity(playVideoIntent)
                }
            }
            withItems(mutableListOf())
        }

        reviewAdapter = mBinding.incBody.detailBodyRecyclerViewReviews.setUpBinding<Review> {
            withLayoutResId(R.layout.item_review)
            onBind<ItemReviewBinding>(BR.data) { _, _ ->
            }
            onClick { _, _, _ ->
            }
            withItems(mutableListOf())
        }
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
            Status.ERROR -> toast(resource.errorEnvelope?.statusMessage.toString())
            Status.LOADING -> { }
        }
    }

    private fun updateVideoList(resource: Resource<List<Video>>) {
        when(resource.status) {
            Status.SUCCESS -> {
                resource.data?.let {
                    videoAdapter?.addAll(it.toMutableList())

                    mBinding.incBody.detailBodyTrailers.visible()
                    mBinding.incBody.detailBodyRecyclerViewTrailers.visible()
                }
            }
            Status.ERROR -> toast(resource.errorEnvelope?.statusMessage.toString())
            Status.LOADING -> { }
        }
    }

    private fun updateReviewList(resource: Resource<List<Review>>) {
        when(resource.status) {
            Status.SUCCESS -> {
                resource.data?.let {
                    reviewAdapter?.addAll(it.toMutableList())

                    mBinding.incBody.detailBodyReviews.visible()
                    mBinding.incBody.detailBodyRecyclerViewReviews.visible()
                }
            }
            Status.ERROR -> toast(resource.errorEnvelope?.statusMessage.toString())
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
}
