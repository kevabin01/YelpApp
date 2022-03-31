package com.codequark.yelp.room

class RoomConstants {
    companion object {
        const val databaseName = "YelpApp.db"
        const val databaseVersion = 1

        const val tableLocalBusiness = "tableLocalBusiness"

        const val columnId: String = "id"
        const val columnAlias: String = "alias"
        const val columnName: String = "name"
        const val columnImageUrl: String = "imageUrl"
        const val columnIsClosed: String = "isClosed"
        const val columnUrl: String = "url"
        const val columnReviewCount: String = "reviewCount"
        const val columnCategories: String = "categories"
        const val columnRating: String = "rating"
        const val columnLatitude: String = "latitude"
        const val columnLongitude: String = "longitude"
        const val columnPrice: String = "price"
        const val columnAddress: String = "address"
        const val columnCity: String = "city"
        const val columnZipCode: String = "zipCode"
        const val columnCountry: String = "country"
        const val columnState: String = "state"
        const val columnDisplayAddress: String = "displayAddress"
        const val columnPhone: String = "phone"
        const val columnDisplayPhone: String = "displayPhone"
        const val columnDistance: String = "distance"
    }
}