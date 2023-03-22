package com.geektech.workwithgooglemap.fragments

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap

class MarkerListener(
    private val mMap: GoogleMap
) {

    fun markerListener() {
        mMap.setOnMarkerClickListener { marker ->
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 18f))
            true
        }
    }
}