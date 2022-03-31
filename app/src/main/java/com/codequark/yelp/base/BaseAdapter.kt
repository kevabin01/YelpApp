package com.codequark.yelp.base

import android.annotation.SuppressLint
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.codequark.yelp.interfaces.ItemListener
import com.codequark.yelp.viewHolders.BindingHolder

abstract class BaseAdapter<L>(
    @NonNull
    var list: List<L>,

    @NonNull
    open val listener: ItemListener<L>
): RecyclerView.Adapter<BindingHolder>() {
    constructor(@NonNull listener: ItemListener<L>): this(emptyList(), listener)

    constructor(@NonNull list: List<L>): this(list, object: ItemListener<L> {})

    constructor(): this(emptyList(), object: ItemListener<L> {})

    @NonNull
    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    open fun setContent(@NonNull list: List<L>) {
        this.list = list
        notifyDataSetChanged()
        listener.onDataSet(list.isEmpty(), list.size)
    }
}