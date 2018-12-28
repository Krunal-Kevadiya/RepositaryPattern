package com.example.ownrepositarypatternsample.ui.person

import android.view.View
import com.example.ownrepositarypatternsample.R
import com.example.ownrepositarypatternsample.base.Resource
import com.example.ownrepositarypatternsample.data.local.entity.Person
import com.skydoves.baserecyclerviewadapter.BaseAdapter
import com.skydoves.baserecyclerviewadapter.BaseViewHolder
import com.skydoves.baserecyclerviewadapter.SectionRow

class PeopleAdapter(val delegate: PeopleViewHolder.Delegate): BaseAdapter() {

    init {
        addSection(ArrayList<Person>())
    }

    fun addPeople(resource: Resource<List<Person>>) {
        resource.data?.let {
            sections[0].addAll(resource.data)
            notifyDataSetChanged()
        }
    }

    override fun layout(sectionRow: SectionRow): Int {
        return R.layout.item_person
    }

    override fun viewHolder(layout: Int, view: View): BaseViewHolder {
        return PeopleViewHolder(view, delegate)
    }
}
