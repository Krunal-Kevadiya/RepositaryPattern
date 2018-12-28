package com.example.ownrepositarypatternsample.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class PaginatorList(private val recyclerView: RecyclerView,
                    private val isLoading: () -> Boolean,
                    private var currentPage: Int = 0,
                    private val loadMore: (Int) -> Unit,
                    private val onLast: () -> Boolean = { true }): RecyclerView.OnScrollListener() {

    var threshold = 0
    var endWithAuto = false
    var initFirstTime = true

    init {
        recyclerView.addOnScrollListener(this)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val layoutManager = recyclerView.layoutManager
        layoutManager?.let {
            val visibleItemCount = it.childCount
            val totalItemCount = it.itemCount
            val firstVisibleItemPosition = when (layoutManager) {
                is LinearLayoutManager -> layoutManager.findLastVisibleItemPosition()
                is GridLayoutManager -> layoutManager.findLastVisibleItemPosition()
                else -> return
            }

            if (onLast() || isLoading()) return

            if(endWithAuto) {
                if(visibleItemCount + firstVisibleItemPosition == totalItemCount) return
            }

            if ((visibleItemCount + firstVisibleItemPosition + threshold) >= totalItemCount && !initFirstTime) {
                loadMore(++currentPage)
            }
        }
    }

    fun resetCurrentPage() {
        this.currentPage = 0
    }
}