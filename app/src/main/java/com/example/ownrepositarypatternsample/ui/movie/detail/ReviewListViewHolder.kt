package com.example.ownrepositarypatternsample.ui.movie.detail

import android.view.View
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Review
import com.skydoves.baserecyclerviewadapter.BaseViewHolder
import kotlinx.android.synthetic.main.item_review.view.*

class ReviewListViewHolder(val view: View): BaseViewHolder(view) {

    private lateinit var review: Review

    override fun bindData(data: Any) {
        if(data is Review) {
            review = data
            drawItem()
        }
    }

    private fun drawItem() {
        itemView.run {
            item_review_title.text = review.author
            item_review_content.text = review.content
        }
    }

    override fun onClick(v: View?) {
    }

    override fun onLongClick(v: View?) = false
}
