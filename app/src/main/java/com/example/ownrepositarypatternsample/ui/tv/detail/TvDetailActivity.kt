package com.example.ownrepositarypatternsample.ui.tv.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.example.ownrepositarypatternsample.R
import com.example.ownrepositarypatternsample.TvDetailBinding
import com.example.ownrepositarypatternsample.base.InjectActivity
import com.example.ownrepositarypatternsample.base.Resource
import com.example.ownrepositarypatternsample.base.Status
import com.example.ownrepositarypatternsample.data.Api
import com.example.ownrepositarypatternsample.data.local.entity.Tv
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Keyword
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Review
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Video
import com.example.ownrepositarypatternsample.ui.movie.detail.ReviewListAdapter
import com.example.ownrepositarypatternsample.ui.movie.detail.VideoListAdapter
import com.example.ownrepositarypatternsample.ui.movie.detail.VideoListViewHolder
import com.example.ownrepositarypatternsample.utils.KeywordListMapper
import com.example.ownrepositarypatternsample.utils.extension.*
import org.jetbrains.anko.toast

class TvDetailActivity : InjectActivity<TvDetailBinding, TvDetailViewModel>(), VideoListViewHolder.Delegate {
    private val videoAdapter by lazy { VideoListAdapter(this) }
    private val reviewAdapter by lazy { ReviewListAdapter() }

    override fun getLayoutId(): Int = R.layout.activity_tv_detail

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initializeUI()
    }

    override fun initObserve() {
        observeLiveData(mViewModel.getKeywordListObservable()) { updateKeywordList(it) }
        mViewModel.postKeywordId(getTvFromIntent().id)

        observeLiveData(mViewModel.getVideoListObservable()) { updateVideoList(it) }
        mViewModel.postVideoId(getTvFromIntent().id)

        observeLiveData(mViewModel.getReviewListObservable()) { updateReviewList(it) }
        mViewModel.postReviewId(getTvFromIntent().id)
    }

    private fun initializeUI() {
        applyToolbarMargin(mBinding.tvDetailToolbar)
        simpleToolbarWithHome(mBinding.tvDetailToolbar, getTvFromIntent().name)
        getTvFromIntent().backdrop_path?.let {
            Glide.with(this).load(Api.getBackdropPath(it))
                    .listener(requestGlideListener(mBinding.tvDetailPoster))
                    .into(mBinding.tvDetailPoster)
        } ?: let {
            Glide.with(this).load(Api.getBackdropPath(getTvFromIntent().poster_path))
                    .listener(requestGlideListener(mBinding.tvDetailPoster))
                    .into(mBinding.tvDetailPoster)
        }

        mBinding.incHeader.detailHeaderTitle.text = getTvFromIntent().name
        mBinding.incHeader.detailHeaderRelease.text = "First Air Date : ${getTvFromIntent().first_air_date}"
        mBinding.incHeader.detailHeaderStar.rating = getTvFromIntent().vote_average / 2
        mBinding.incBody.detailBodyRecyclerViewTrailers.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mBinding.incBody.detailBodyRecyclerViewTrailers.adapter = videoAdapter
        mBinding.incBody.detailBodySummary.text = getTvFromIntent().overview
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

    private fun getTvFromIntent(): Tv {
        return intent.getParcelableExtra("tv") as Tv
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
