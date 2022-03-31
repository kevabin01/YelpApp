package com.codequark.yelp.room.models

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codequark.yelp.room.RoomConstants
import java.util.*

@Entity(tableName = RoomConstants.tableLocalBusiness)
data class LocalBusiness(
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = RoomConstants.columnId)
    val id: String,

    @NonNull
    @ColumnInfo(name = RoomConstants.columnAlias)
    val alias: String,

    @NonNull
    @ColumnInfo(name = RoomConstants.columnName)
    val name: String,

    @NonNull
    @ColumnInfo(name = RoomConstants.columnImageUrl)
    val imageUrl: String,

    @NonNull
    @ColumnInfo(name = RoomConstants.columnIsClosed)
    val isClosed: Boolean,

    @NonNull
    @ColumnInfo(name = RoomConstants.columnUrl)
    val url: String,

    @NonNull
    @ColumnInfo(name = RoomConstants.columnReviewCount)
    val reviewCount: Int,

    @NonNull
    @ColumnInfo(name = RoomConstants.columnCategories)
    val categories: String,

    @NonNull
    @ColumnInfo(name = RoomConstants.columnRating)
    val rating: Double,

    @NonNull
    @ColumnInfo(name = RoomConstants.columnLatitude)
    val latitude: Double,

    @NonNull
    @ColumnInfo(name = RoomConstants.columnLongitude)
    val longitude: Double,

    @NonNull
    @ColumnInfo(name = RoomConstants.columnPrice)
    val price: String?,

    @Nullable
    @ColumnInfo(name = RoomConstants.columnAddress)
    val address: String,

    @NonNull
    @ColumnInfo(name = RoomConstants.columnCity)
    val city: String,

    @NonNull
    @ColumnInfo(name = RoomConstants.columnZipCode)
    val zipCode: String,

    @NonNull
    @ColumnInfo(name = RoomConstants.columnCountry)
    val country: String,

    @NonNull
    @ColumnInfo(name = RoomConstants.columnState)
    val state: String,

    @NonNull
    @ColumnInfo(name = RoomConstants.columnDisplayAddress)
    val displayAddress: String,

    @NonNull
    @ColumnInfo(name = RoomConstants.columnPhone)
    val phone: String,

    @NonNull
    @ColumnInfo(name = RoomConstants.columnDisplayPhone)
    val displayPhone: String,

    @NonNull
    @ColumnInfo(name = RoomConstants.columnDistance)
    val distance: Double
) {
    override fun equals(other: Any?): Boolean {
        if(this === other) {
            return true
        }

        if(other !is LocalBusiness) {
            return false
        }

        return id == other.id &&
            alias == other.alias &&
            name == other.name &&
            imageUrl == other.imageUrl &&
            isClosed == other.isClosed &&
            url == other.url &&
            reviewCount == other.reviewCount &&
            categories == other.categories &&
            rating == other.rating &&
            latitude == other.latitude &&
            longitude == other.longitude &&
            price == other.price &&
            address == other.address &&
            city == other.city &&
            zipCode == other.zipCode &&
            country == other.country &&
            state == other.state &&
            displayAddress == other.displayAddress &&
            phone == other.phone &&
            displayPhone == other.displayPhone &&
            distance == other.distance
    }

    override fun hashCode(): Int {
        return Objects.hash(id, alias, name, imageUrl, isClosed, url, reviewCount, categories, rating, latitude, longitude, price, address, city, zipCode, country, state, displayAddress, phone, displayPhone, distance)
    }
}