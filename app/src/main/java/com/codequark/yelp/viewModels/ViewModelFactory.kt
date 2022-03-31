package com.codequark.yelp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codequark.yelp.managers.ResourceManager

@Suppress("UNCHECKED_CAST") // Guaranteed to succeed at this point.
class ViewModelFactory: ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        val application = ResourceManager.getInstance().application

        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(application) as T
            }

            modelClass.isAssignableFrom(NetworkViewModel::class.java) -> {
                NetworkViewModel(application) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}