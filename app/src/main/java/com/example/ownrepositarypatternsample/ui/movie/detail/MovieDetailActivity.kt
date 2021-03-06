package com.example.ownrepositarypatternsample.ui.movie.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ownrepositarypatternsample.BR
import com.example.ownrepositarypatternsample.R
import com.example.ownrepositarypatternsample.base.BaseActivity
import com.example.ownrepositarypatternsample.base.repository.ScreenState
import com.example.ownrepositarypatternsample.data.Api
import com.example.ownrepositarypatternsample.data.local.entity.Movie
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Keyword
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Review
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Video
import com.example.ownrepositarypatternsample.databinding.ActivityMovieDetailBinding
import com.example.ownrepositarypatternsample.databinding.ItemReviewBinding
import com.example.ownrepositarypatternsample.databinding.ItemVideoBinding
import com.example.ownrepositarypatternsample.utils.KeywordListMapper
import com.example.ownrepositarypatternsample.utils.extension.requestGlideListener
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette
import com.kotlinlibrary.recycleradapter.setUpBinding
import com.kotlinlibrary.recycleradapter.simple.SingleBindingAdapter
import com.kotlinlibrary.utils.arguments.bindArgument
import com.kotlinlibrary.utils.ktx.applyToolbarMargin
import com.kotlinlibrary.utils.ktx.observeLiveData
import com.kotlinlibrary.utils.ktx.simpleToolbarWithHome
import com.kotlinlibrary.utils.ktx.visible

class MovieDetailActivity : BaseActivity<ActivityMovieDetailBinding, MovieDetailViewModel>(R.layout.activity_movie_detail) {
    private var videoAdapter: SingleBindingAdapter<Video>? = null
    private var reviewAdapter: SingleBindingAdapter<Review>? = null

    private val movie: Movie by bindArgument("movie")

    override fun initObserve() {
        observeLiveData(mViewModel.getKeywordListObservable()) { updateKeywordList(it) }
        mViewModel.postKeywordId(movie.id)

        observeLiveData(mViewModel.getVideoListObservable()) { updateVideoList(it) }
        mViewModel.postVideoId(movie.id)

        observeLiveData(mViewModel.getReviewListObservable()) { updateReviewList(it) }
        mViewModel.postReviewId(movie.id)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initializeUI()
    }

    private fun initializeUI() {
        applyToolbarMargin(mBinding.movieDetailToolbar)
        simpleToolbarWithHome(mBinding.movieDetailToolbar, movie.title)
        movie.backdropPath?.let {
            Glide.with(this).load(Api.getBackdropPath(it))
                .listener(requestGlideListener(mBinding.movieDetailPoster))
                .into(mBinding.movieDetailPoster)
        } ?: let {
            Glide.with(this).load(Api.getBackdropPath(movie.posterPath!!))
                .listener(requestGlideListener(mBinding.movieDetailPoster))
                .into(mBinding.movieDetailPoster)
        }
        mBinding.incHeader.detailHeaderTitle.text = movie.title
        mBinding.incHeader.detailHeaderRelease.text = "Release Date : ${movie.releaseDate}"
        mBinding.incHeader.detailHeaderStar.rating = movie.voteAverage / 2
        mBinding.incBody.detailBodySummary.text = movie.overview

        videoAdapter = mBinding.incBody.detailBodyRecyclerViewTrailers.setUpBinding<Video> {
            withLayoutManager(LinearLayoutManager(this@MovieDetailActivity, LinearLayoutManager.HORIZONTAL, false))
            withLayoutResId(R.layout.item_video)
            onBind<ItemVideoBinding>(BR.data) { _, item ->
                item.key?.let {
                    Glide.with(this@MovieDetailActivity)
                        .load(Api.getYoutubeThumbnailPath(it))
                        .listener(
                            GlidePalette.with(Api.getYoutubeThumbnailPath(it))
                                .use(BitmapPalette.Profile.VIBRANT)
                                .intoBackground(itemVideoPalette)
                                .crossfade(true)
                        )
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

    private fun updateKeywordList(resource: ScreenState<List<Keyword>>) {
        when (resource) {
            is ScreenState.LoadingState.Show -> {
                showAlertView(true)
            }
            is ScreenState.LoadingState.Hide -> {
                showAlertView(false)
            }
            is ScreenState.SuccessState.Api -> {
                resource.data?.let {
                    mBinding.incBody.detailBodyTags.tags = KeywordListMapper.mapToStringList(it)
                }
                if (!resource.data.isNullOrEmpty()) {
                    mBinding.incBody.detailBodyTags.visible()
                }
            }
            is ScreenState.ErrorState.Api -> {
                mViewModel.message.postValue(resource.message)
            }
            else -> {}
        }
    }

    private fun updateVideoList(resource: ScreenState<List<Video>>) {
        when (resource) {
            is ScreenState.LoadingState.Show -> {
                showAlertView(true)
            }
            is ScreenState.LoadingState.Hide -> {
                showAlertView(false)
            }
            is ScreenState.SuccessState.Api -> {
                resource.data?.let {
                    videoAdapter?.addAll(it.toMutableList())

                    mBinding.incBody.detailBodyTrailers.visible()
                    mBinding.incBody.detailBodyRecyclerViewTrailers.visible()
                }
            }
            is ScreenState.ErrorState.Api -> {
                mViewModel.message.postValue(resource.message)
            }
            else -> {}
        }
    }

    private fun updateReviewList(resource: ScreenState<List<Review>>) {
        when (resource) {
            is ScreenState.LoadingState.Show -> {
                showAlertView(true)
            }
            is ScreenState.LoadingState.Hide -> {
                showAlertView(false)
            }
            is ScreenState.SuccessState.Api -> {
                resource.data?.let {
                    reviewAdapter?.addAll(it.toMutableList())

                    mBinding.incBody.detailBodyReviews.visible()
                    mBinding.incBody.detailBodyRecyclerViewReviews.visible()
                }
            }
            is ScreenState.ErrorState.Api -> {
                mViewModel.message.postValue(resource.message)
            }
            else -> {}
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return false
    }
}
