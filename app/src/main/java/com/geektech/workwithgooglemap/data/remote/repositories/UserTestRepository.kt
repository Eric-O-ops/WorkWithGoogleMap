package com.geektech.workwithgooglemap.data.remote.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.auth.User
import java.net.UnknownServiceException
import javax.inject.Inject

class UserTestRepository @Inject constructor(
    private val db: FirebaseFirestore
) {

    fun addUserTest(name: String, location: GeoPoint) {
        val user = hashMapOf(
            "name" to name,
            "location" to location
        )

        db.collection("UsersTest").document(name)
            .set(user)
            .addOnSuccessListener { Log.d("TAGGER", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("TAGGER", "Error writing document", e) }
        /*  db.collection("UsersTest")
              .add(user)
              .addOnSuccessListener { Log.d("TAGGER", "DocumentSnapshot successfully written!") }
              .addOnFailureListener { e -> Log.w("TAGGER", "Error writing document", e) }*/
    }

    fun fetchUserTest() {
        val users: ArrayList<Users> = ArrayList()
        db.collection("UsersTest")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("TAGGER", "Listen failed.", e)
                    return@addSnapshotListener
                }

                for (doc in value!!) {
                    Log.d("TAGGER", "${doc.getString("name")} => ${doc.getString("name")}")
                    Log.d(
                        "TAGGER",
                        "${doc.getGeoPoint("location")} => ${doc.getGeoPoint("location")}"
                    )
                    val name = doc.getString("name")
                    val geoPoint = doc.getGeoPoint("location")
                    users.add(Users(name!!, geoPoint!!))

                }
                Log.d("TAGGER", "Current cites in CA: $users")

            }
    }
}