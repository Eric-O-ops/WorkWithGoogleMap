package com.geektech.workwithgooglemap.fragments.map

import android.location.Location
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.geektech.workwithgooglemap.data.remote.repositories.UserRepository
import com.geektech.workwithgooglemap.data.models.Users
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: UserRepository,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private var _users: MutableLiveData<ArrayList<Users>> = MutableLiveData()

   /* init {
        viewModelScope.launch { listenUpdatesUsersLocation() }
    }*/

    fun updateThisUserLocation(name: String, location: Location) {

        repository.updateThisUserLocation(name, location)
    }

    suspend fun listenUpdatesUsersLocation(): Flow<ArrayList<Users>> {
        firestore.collection("UsersTest")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("TAGGER", "Listen failed.", e)
                    return@addSnapshotListener
                }

                val allUsers: ArrayList<Users> = ArrayList()
                for (doc in value!!) {
                    if(doc.getString("name") != "marsel") {
                        allUsers.add(
                            Users(
                                doc.getLong("id")!!.toInt(),
                                doc.getString("name")!!,
                                doc.getGeoPoint("location")!!
                            )
                        )
                    }
                }
                _users.value = allUsers
            }
        return _users.asFlow()
    }

    internal var user: MutableLiveData<ArrayList<Users>>
        get() {
            return _users
        }
        set(value) {
            _users = value
        }
}