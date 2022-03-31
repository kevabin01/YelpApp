package com.codequark.yelp.models.business

import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import com.codequark.yelp.utils.Constants.ItemDef

data class DetailItem(
    @ItemDef
    val id: Int,

    @DrawableRes
    val icon: Int,

    @NonNull
    val title: String,

    @NonNull
    val text: String
) {
    constructor(@ItemDef id: Int, @DrawableRes icon: Int, @NonNull text: String): this(id, icon, "", text)

    constructor(@ItemDef id: Int, @NonNull title: String): this(id, 0, title, "")

    constructor(@ItemDef id: Int): this(id, 0, "", "")
}