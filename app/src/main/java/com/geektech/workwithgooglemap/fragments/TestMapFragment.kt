package com.geektech.workwithgooglemap.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.geektech.workwithgooglemap.R
import com.geektech.workwithgooglemap.data.remote.repositories.ConvertToBitmap
import com.geektech.workwithgooglemap.data.remote.repositories.UserTestRepository
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TestMapFragment : Fragment(R.layout.fragment_test_map), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var userMarker: Marker? = null
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @Inject
    lateinit var repository: UserTestRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        init()
    }

    private fun init() {
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 500)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(2000)
            .setMaxUpdateDelayMillis(100)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                Log.e("TAGGER", "onLocationResult: ${locationResult.lastLocation}")
                if (mMap != null) setUserLocationMarker(locationResult.lastLocation!!)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        markerListener()
    }

    private fun markerListener() {
        mMap.setOnMarkerClickListener { marker ->
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 18f))
            true
        }
    }

    private fun setUserLocationMarker(location: Location) {

        val covertImage = ConvertToBitmap.Base()
        val userLocation = LatLng(location.latitude, location.longitude)
        val userGeoPoint = GeoPoint(location.latitude, location.longitude)
        repository.addUserTest("marsel", userGeoPoint)

        if (userMarker == null) {
            userMarker = mMap.addMarker(
                MarkerOptions()
                    .position(userLocation)
                    .icon(
                        covertImage.convert(
                            requireActivity().applicationContext,
                            R.drawable.markofme2
                        )
                    )
                    .rotation(location.bearing)
                    .anchor(0.5f, 0.5f)
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 18f))
        } else {
            userMarker?.let { marker ->
                marker.position = userLocation
                marker.rotation = location.bearing
            }
        }
    }

    private fun setOtherUserLocationMarker() {

    }

    private fun startLocationUpdates() {
        checkPermission()
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() = fusedLocationClient.removeLocationUpdates(locationCallback)

    override fun onStart() {
        super.onStart()

        startLocationUpdates()
    }

    override fun onStop() {
        super.onStop()

        stopLocationUpdates()
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0
            )
            return
        }
    }
}