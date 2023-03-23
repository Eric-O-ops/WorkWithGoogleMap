package com.geektech.workwithgooglemap.data.models

import com.google.firebase.firestore.GeoPoint

data class Users(
    val name: String,
    var location: GeoPoint
)