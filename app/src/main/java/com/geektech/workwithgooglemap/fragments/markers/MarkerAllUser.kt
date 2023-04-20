package com.geektech.workwithgooglemap.fragments.markers

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.geektech.workwithgooglemap.R
import com.geektech.workwithgooglemap.data.models.Users
import com.geektech.workwithgooglemap.fragments.ConvertToBitmap
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.delay

/**
 * This class display and update information about users
 *
 * "display()" for add and update location on map
 * "zoomChanged()" for update icon of markers when zoom <= 19
 *
 * In constructor of the class add only application context in "applicationContext"
 * */

class MarkerAllUser(
    private val context: Context,
    private val applicationContext: Context
) {
    private var usersMarker: ArrayList<Marker> = ArrayList()
    private val convertImage = ConvertToBitmap.Base()
    private var init = false
    private val quickSort = QuickSort()

    suspend fun display(usersList: ArrayList<Users>, mMap: GoogleMap) {
        val markerOnMap = if (usersMarker.isEmpty()) 0 else usersMarker.size - 1
        val markerOnDb = usersList.size
        var buffer = usersList
        if (markerOnMap < markerOnDb) {
            buffer = quickSort.sort(usersList)
            for (value in markerOnMap until markerOnDb) {
                delay(2000L)
                val user = buffer[value]
                val position = LatLng(user.location.latitude, user.location.longitude)
                val marker = mMap.addMarker(
                    MarkerOptions()
                        .position(position)
                        .title(user.name)
                        .icon(
                            convertImage.convert(context, R.drawable.marker_other_user)
                        )
                )
                usersMarker.add(marker!!)
            }
            if (init) display(usersList, mMap)
            init = true
        } else {
            try {
                for (value in 0..markerOnMap) {
                    delay(2000L)
                    val user = buffer[value]
                    val position = LatLng(user.location.latitude, user.location.longitude)
                    usersMarker[value].apply {
                        this.position = position
                    }
                }
            } catch (e:Exception) {
                Log.e("NPE", e.toString() )
            }
        }
    }

   suspend fun zoomChanged(zoom: Float) {
       delay(2000L)
        val markerView =
            LayoutInflater.from(context).inflate(R.layout.marker_other_person, null)
        val container = markerView.findViewById<LinearLayout>(R.id.container_marker)
        val textUnderIcon = markerView.findViewById<TextView>(R.id.user_name)
        for (value in 0 until usersMarker.size) {
            val marker = usersMarker[value]
            textUnderIcon.text = marker.title
            if (zoom <= 19) {
                val covertImage = ConvertToBitmap.Base()
                marker.setIcon(
                    covertImage.convert(
                        applicationContext,
                        R.drawable.marker_other_user
                    )
                )
            } else {
                marker.setIcon(
                    BitmapDescriptorFactory
                        .fromBitmap(
                            ConvertToBitmap.Base()
                                .convert(container)
                        )
                )
            }
        }
    }
}