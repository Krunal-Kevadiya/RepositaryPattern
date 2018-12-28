package com.example.ownrepositarypatternsample.ui.movie.detail

import android.view.View
import com.example.ownrepositarypatternsample.R
import com.example.ownrepositarypatternsample.base.Resource
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Review
import com.skydoves.baserecyclerviewadapter.BaseAdapter
import com.skydoves.baserecyclerviewadapter.BaseViewHolder
import com.skydoves.baserecyclerviewadapter.SectionRow

class ReviewListAdapter: BaseAdapter() {

    init {
        addSection(ArrayList<Review>())
    }

    fun addReviewList(resource: Resource<List<Review>>) {
        resource.data?.let {
            sections[0].addAll(it)
        }
        notifyDataSetChanged()
    }

    override fun layout(sectionRow: SectionRow): Int {
        return R.layout.item_review
    }

    override fun viewHolder(layout: Int, view: View): BaseViewHolder {
        return ReviewListViewHolder(view)
    }
}
