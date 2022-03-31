package com.codequark.yelp.viewModels

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.codequark.yelp.repositories.NetworkRepository

class NetworkViewModel(@NonNull application: Application): AndroidViewModel(application) {
    @NonNull
    val repository = NetworkRepository.getInstance(application.applicationContext)

    @NonNull
    val network: LiveData<Int> = repository.getNetworkState()
}