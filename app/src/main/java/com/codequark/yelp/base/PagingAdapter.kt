package com.codequark.yelp.base

import android.annotation.SuppressLint
import androidx.annotation.NonNull
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.codequark.yelp.interfaces.ItemListener
import com.codequark.yelp.viewHolders.BindingHolder

abstract class PagingAdapter<R: Any>(
    diffCallback: DiffUtil.ItemCallback<R>,
    val listener: ItemListener<R>
): PagingDataAdapter<R, BindingHolder>(diffCallback) {
    @NonNull
    private val loadStateListener = { combinedLoadStates: CombinedLoadStates ->
        val loadState = combinedLoadStates.source.refresh

        if(loadState is LoadState.NotLoading) {
            val itemCount = itemCount
            listener.onDataSet(itemCount == 0, itemCount)
        }
    }

    init {
        addLoadStateListener(loadStateListener)
    }

    open fun getContent(position: Int): R? {
        return super.getItem(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    open fun update() {
        notifyDataSetChanged()
    }
}