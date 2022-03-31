package com.codequark.yelp.ui.adapters.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import com.codequark.yelp.base.BaseAdapter
import com.codequark.yelp.databinding.ItemSearchBinding
import com.codequark.yelp.interfaces.ItemListener
import com.codequark.yelp.managers.ImageManager
import com.codequark.yelp.models.business.Business
import com.codequark.yelp.viewHolders.BindingHolder

class SearchAdapter(listener: ItemListener<Business>): BaseAdapter<Business>(listener) {
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(bindingHolder: BindingHolder, position: Int) {
        if(bindingHolder.binding is ItemSearchBinding) {
            val binding = bindingHolder.binding
            val item: Business = list[position]

            binding.tvNombre.text = item.name

            var categories = ""
            item.categories.forEach{
                categories += "${it.title}\n"
            }

            categories = categories.replace("\n", ", ")
            categories = categories.substring(0, categories.length)
            categories = categories.dropLast(2)

            binding.tvCategoria.text = categories

            val location = item.location.address1
            binding.tvDireccion.text = location

            ImageManager.instance.setImage(item.imageUrl, binding.ivImagen)

            binding.container.setOnClickListener {
                listener.onItemSelected(item)
            }

            binding.ivImagen.setOnClickListener {
                listener.onItemSelected(item)
            }
        }
    }
}