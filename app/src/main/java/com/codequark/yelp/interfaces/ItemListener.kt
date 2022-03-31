package com.codequark.yelp.interfaces

import androidx.annotation.NonNull

interface ItemListener<Model> {
    fun onItemSelected(@NonNull item: Model) {}

    fun onDataSet(@NonNull isEmpty: Boolean, @NonNull itemCount: Int) {}
}