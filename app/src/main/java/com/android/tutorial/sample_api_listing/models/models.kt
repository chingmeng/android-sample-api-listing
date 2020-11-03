package com.android.tutorial.sample_api_listing.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Shop(
    var id: Int,
    var imageUrl: String,
    var isClose: Boolean,
    var closeLabel: String,
    var productName: String,
    var productDesc: String,
    var star: Int,
    var distance: String,
    var promoDesc: String,
    var outletAround: Int
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        other?.let {
            return this.id == (it as Shop).id
        }
        return false
    }

    override fun hashCode(): Int {
        return this.id
    }
}