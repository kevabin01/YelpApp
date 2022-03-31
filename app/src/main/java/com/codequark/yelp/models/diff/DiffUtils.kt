package com.codequark.yelp.models.diff

import androidx.recyclerview.widget.DiffUtil
import com.codequark.yelp.room.models.LocalBusiness

class DiffUtils {
    companion object {
        val LOCAL_BUSINESS_ITEM_CALLBACK: DiffUtil.ItemCallback<LocalBusiness> = object: DiffUtil.ItemCallback<LocalBusiness>() {
            override fun areItemsTheSame(oldItem: LocalBusiness, newItem: LocalBusiness): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: LocalBusiness, newItem: LocalBusiness): Boolean {
                return oldItem == newItem
            }
        }
    }
}