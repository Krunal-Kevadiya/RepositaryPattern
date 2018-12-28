package com.example.ownrepositarypatternsample.ui.movie.detail

import android.view.View
import com.example.ownrepositarypatternsample.R
import com.example.ownrepositarypatternsample.base.Resource
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Video
import com.skydoves.baserecyclerviewadapter.BaseAdapter
import com.skydoves.baserecyclerviewadapter.BaseViewHolder
import com.skydoves.baserecyclerviewadapter.SectionRow

class VideoListAdapter(private val delegate: VideoListViewHolder.Delegate): BaseAdapter() {

    init {
        addSection(ArrayList<Video>())
    }

    fun addVideoList(resource: Resource<List<Video>>) {
        resource.data?.let {
            sections[0].addAll(it)
        }
        notifyDataSetChanged()
    }

    override fun layout(sectionRow: SectionRow): Int {
        return R.layout.item_video
    }

    override fun viewHolder(layout: Int, view: View): BaseViewHolder {
        return VideoListViewHolder(view, delegate)
    }
}
