package com.example.ownrepositarypatternsample.ui.tv

import android.view.View
import com.example.ownrepositarypatternsample.R
import com.example.ownrepositarypatternsample.base.Resource
import com.example.ownrepositarypatternsample.data.local.entity.Tv
import com.skydoves.baserecyclerviewadapter.BaseAdapter
import com.skydoves.baserecyclerviewadapter.BaseViewHolder
import com.skydoves.baserecyclerviewadapter.SectionRow

class TvListAdapter(private val delegate: TvListViewHolder.Delegate): BaseAdapter() {

    init {
        addSection(ArrayList<Tv>())
    }

    fun addTvList(resource: Resource<List<Tv>>) {
        resource.data?.let {
            sections[0].addAll(it)
            notifyDataSetChanged()
        }
    }

    override fun layout(sectionRow: SectionRow): Int {
        return R.layout.item_poster
    }

    override fun viewHolder(layout: Int, view: View): BaseViewHolder {
        return TvListViewHolder(view, delegate)
    }
}
