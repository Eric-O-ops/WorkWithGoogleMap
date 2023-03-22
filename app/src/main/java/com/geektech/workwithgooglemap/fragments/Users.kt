package com.geektech.workwithgooglemap.fragments

import com.google.firebase.firestore.GeoPoint

data class Users(
    val name: String,
    var location: GeoPoint
)