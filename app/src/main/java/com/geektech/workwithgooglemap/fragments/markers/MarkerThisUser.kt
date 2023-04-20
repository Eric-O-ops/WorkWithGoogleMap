package com.geektech.workwithgooglemap.fragments.markers

import android.content.Context
import android.location.Location
import com.geektech.workwithgooglemap.R
import com.geektech.workwithgooglemap.fragments.ConvertToBitmap
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MarkerThisUser {

    private var marker: Marker? = null

   operator fun invoke(location: Location, mMap: GoogleMap, context: Context) {
        val covertImage = ConvertToBitmap.Base()
        val position = LatLng(location.latitude, location.longitude)
        if (marker == null) {
            marker = mMap.addMarker(
                MarkerOptions()
                    .position(position)
                    .icon(
                        covertImage.convert(
                            context,
                            R.drawable.mark_of_this_user
                        )
                    )
                    .rotation(location.bearing)
                    .anchor(0.5f, 0.5f)
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 18f))
        } else {
            marker?.let { marker ->
                marker.position = position
                marker.rotation = location.bearing
            }
        }
    }
}