package com.codequark.yelp.ui.adapters.base

import android.view.LayoutInflater
import android.view.ViewGroup
import com.codequark.yelp.base.BaseAdapter
import com.codequark.yelp.databinding.ItemContentBinding
import com.codequark.yelp.databinding.ItemDividerBinding
import com.codequark.yelp.databinding.ItemHeaderBinding
import com.codequark.yelp.models.business.DetailItem
import com.codequark.yelp.utils.Constants.ItemDef
import com.codequark.yelp.viewHolders.BindingHolder

class DetailAdapter: BaseAdapter<DetailItem>() {
    override fun getItemViewType(position: Int): Int {
        val item = list[position]
        val id = item.id

        if(id == ItemDef.HEADER) {
            return ItemDef.HEADER
        } else if(id == ItemDef.DIVIDER) {
            return ItemDef.DIVIDER
        }

        return ItemDef.CONTENT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        return when (viewType) {
            ItemDef.HEADER -> {
                val binding = ItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BindingHolder(binding)
            }

            ItemDef.DIVIDER -> {
                val binding = ItemDividerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BindingHolder(binding)
            }

            else -> {
                val binding = ItemContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BindingHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(bindingHolder: BindingHolder, position: Int) {
        val item = list[position]

        when(bindingHolder.binding) {
            is ItemHeaderBinding -> {
                val holder = bindingHolder.binding

                holder.tvTitle.text = item.title
            }

            is ItemContentBinding -> {
                val holder = bindingHolder.binding

                holder.tvTitle.text = item.title

                holder.tvText.text = item.text

                holder.ivImage.setImageResource(item.icon)

                holder.container.setOnClickListener {
                    listener.onItemSelected(item)
                }
            }
        }
    }
}