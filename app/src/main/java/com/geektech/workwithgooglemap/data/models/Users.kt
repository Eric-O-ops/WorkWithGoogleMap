package com.geektech.workwithgooglemap.data.models

import com.google.firebase.firestore.GeoPoint

data class Users(
    val id: Int,
    val name: String,
    var location: GeoPoint
)