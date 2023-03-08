package com.geektech.workwithgooglemap.fragments

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geektech.workwithgooglemap.data.remote.repositories.UserTestRepository
import com.geektech.workwithgooglemap.data.remote.repositories.Users
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BlankViewModel @Inject constructor(
    private val repository: UserTestRepository,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private var _users: MutableLiveData<ArrayList<Users>> = MutableLiveData()

    init {

        listenUpdatesUsersLocation()
    }

    private fun listenUpdatesUsersLocation() {
        firestore.collection("UsersTest")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("TAGGER", "Listen failed.", e)
                    return@addSnapshotListener
                }

                val allUsers: ArrayList<Users> = ArrayList()
                for (doc in value!!) {
                    val name = doc.getString("name")
                    val geoPoint = doc.getGeoPoint("location")
                    allUsers.add(Users(name!!, geoPoint!!))

                }
                _users.value = allUsers
            }
    }
    internal var user: MutableLiveData<ArrayList<Users>>
        get() { return _users }
        set(value) {_users = value}
}