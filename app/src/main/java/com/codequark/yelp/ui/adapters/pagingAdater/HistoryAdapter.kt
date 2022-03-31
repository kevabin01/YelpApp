package com.codequark.yelp.ui.adapters.pagingAdater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import com.codequark.yelp.base.PagingAdapter
import com.codequark.yelp.databinding.ItemHistoryBinding
import com.codequark.yelp.interfaces.ItemListener
import com.codequark.yelp.managers.ImageManager
import com.codequark.yelp.models.diff.DiffUtils
import com.codequark.yelp.room.models.LocalBusiness
import com.codequark.yelp.viewHolders.BindingHolder

class HistoryAdapter(listener: ItemListener<LocalBusiness>): PagingAdapter<LocalBusiness>(DiffUtils.LOCAL_BUSINESS_ITEM_CALLBACK, listener) {
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(@NonNull holder: BindingHolder, @NonNull position: Int) {
        val item = getItem(position) ?: return

        if(holder.binding is ItemHistoryBinding) {
            val binding: ItemHistoryBinding = holder.binding

            ImageManager.instance.setImage(item.imageUrl, binding.ivImage)

            binding.tvName.text = item.name

            binding.container.setOnClickListener {
                listener.onItemSelected(item)
            }

            binding.tvName.setOnClickListener {
                listener.onItemSelected(item)
            }

            binding.ivImage.setOnClickListener {
                listener.onItemSelected(item)
            }
        }
    }
}