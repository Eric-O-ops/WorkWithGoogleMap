package com.geektech.workwithgooglemap.activity

import android.os.Looper
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektech.workwithgooglemap.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.geektech.workwithgooglemap.databinding.ActivityMapsBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(R.layout.activity_maps) {

   /* private val binding by viewBinding(ActivityMapsBinding::bind)
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private var userMarker: Marker? = null
    private lateinit var locationCallback: LocationCallback*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

      /*  val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
*/
       //init()
    }

   /* private fun init() {
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 500)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(2000)
            .setMaxUpdateDelayMillis(100)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                p0 ?: return
                Log.e("TAGGER", "onLocationResult: ${p0.lastLocation}")
                if (mMap != null) setUserLocationMarker(p0.lastLocation!!)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    private fun setUserLocationMarker(location: Location) {

        val userLocation = LatLng(location.latitude, location.longitude)

        if (userMarker == null) {
            userMarker = mMap.addMarker(
                MarkerOptions()
                    .position(userLocation)
                    .icon(bitmapImage(applicationContext, R.drawable.markofme2))
                    .rotation(location.bearing)
                    .anchor(0.5f,0.5f))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 18f))
        } else {
            userMarker?.let {marker ->
                marker.position = userLocation
                marker.rotation = location.bearing
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 18f))
        }
    }

    private fun startLocationUpdates() {
        checkPermission()
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

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
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                0
            )
            return
        }
    }

    private fun bitmapImage(context: Context, image: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, image)
        vectorDrawable?.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable!!.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
*/
    /*private fun userLocation(mMap: GoogleMap) {
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                Log.e(
                    "TAGGGER",
                    "my position: longitude ${location?.longitude} and latitude ${location?.latitude}"
                )
                val userLocation = LatLng(location!!.latitude, location.longitude)
                mMap.addMarker(
                    MarkerOptions()
                        .position(userLocation)
                        .icon(bitmapImage(applicationContext,R.drawable.markofme))
                )
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,18f))
            }

    }*/
}
