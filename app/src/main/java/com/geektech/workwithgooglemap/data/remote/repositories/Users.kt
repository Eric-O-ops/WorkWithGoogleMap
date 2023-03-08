package com.geektech.workwithgooglemap.data.remote.repositories

import com.google.firebase.firestore.GeoPoint

data class Users(
    val name: String,
    var location: GeoPoint
)
