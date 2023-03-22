package com.geektech.workwithgooglemap.data.remote.repositories

import android.location.Location
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val db: FirebaseFirestore
) {

    fun updateThisUserLocation(name: String, location: Location) {

        val user = hashMapOf(
            "name" to name,
            "location" to GeoPoint(location.latitude,location.longitude)
        )

        db.collection("UsersTest").document(name)
            .set(user)
            .addOnSuccessListener { Log.d("TAGGER", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("TAGGER", "Error writing document", e) }

    }
}