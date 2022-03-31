package com.codequark.yelp.viewModels

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.codequark.yelp.repositories.MainRepository
import com.codequark.yelp.room.models.LocalBusiness
import kotlinx.coroutines.flow.Flow

open class DaoViewModel(application: Application): AndroidViewModel(application) {
    @NonNull
    protected val repository = MainRepository.getInstance(application.applicationContext)

    @NonNull
    val localBusinesses: Flow<PagingData<LocalBusiness>> = repository.getLocalBusinesses().cachedIn(viewModelScope)
}