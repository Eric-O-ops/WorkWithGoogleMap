package com.geektech.workwithgooglemap.fragments.map

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.geektech.workwithgooglemap.R
import com.geektech.workwithgooglemap.fragments.ConvertToBitmap
import com.geektech.workwithgooglemap.fragments.MarkerTapListener
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_test_map), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var markerThisUser: Marker? = null
    private var usersMarker: ArrayList<Marker> = ArrayList()
    private val viewModel: MapViewModel by viewModels()
    private lateinit var locRequest: LocationRequest
    private lateinit var locCallback: LocationCallback
    private lateinit var fusedLocClient: FusedLocationProviderClient
    private lateinit var locUpdates: MapLocUpdates
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        uiScope.launch(Dispatchers.IO) {
            initialization()

        }
    }

    private fun initialization() {

        fusedLocClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        locRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 500)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(2000)
            .setMaxUpdateDelayMillis(100)
            .build()

        locThisUser()

        locUpdates = MapLocUpdates(fusedLocClient, locRequest, locCallback)
    }

   private fun zoomCheckListener(userName: String, marker: Marker, zoom: Float) {
        val markerView =
            LayoutInflater.from(requireContext()).inflate(R.layout.marker_other_person, null)
        val container = markerView.findViewById<LinearLayout>(R.id.container_marker)
        val textUnderIcon = markerView.findViewById<TextView>(R.id.user_name)
        textUnderIcon.text = userName
        if (zoom <= 19) {
            val covertImage = ConvertToBitmap.Base()
            marker.setIcon(
                covertImage.convert(
                    requireActivity().applicationContext,
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

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        uiScope.launch {
            MarkerTapListener(googleMap).invoke()
            locOtherUsers()
        }
        mMap.setOnCameraMoveStartedListener {
            val zoom = mMap.cameraPosition.zoom
            Log.e("Zoom", "zoom: $zoom")
            for (value in 0 until usersMarker.size) {
                val user = usersMarker[value]
                zoomCheckListener(user.title.toString(), user, zoom)
            }
        }
    }

    private fun markerThisUser(location: Location) {
        val covertImage = ConvertToBitmap.Base()
        val position = LatLng(location.latitude, location.longitude)
        if (markerThisUser == null) {
            markerThisUser = mMap.addMarker(
                MarkerOptions()
                    .position(position)
                    .icon(
                        covertImage.convert(
                            requireActivity().applicationContext,
                            R.drawable.mark_of_this_user
                        )
                    )
                    .rotation(location.bearing)
                    .anchor(0.5f, 0.5f)
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 18f))
        } else {
            markerThisUser?.let { marker ->
                marker.position = position
                marker.rotation = location.bearing
            }
        }
    }

    private fun locThisUser() {
        locCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                Log.e("TAGGER", "onLocationResult: ${locationResult.lastLocation}")
                if (mMap != null) {
                    markerThisUser(locationResult.lastLocation!!)
                    viewModel.updateThisUserLocation("marsel", locationResult.lastLocation!!)
                }
            }
        }
    }

    private fun locOtherUsers() {
        val convertImage = ConvertToBitmap.Base()
        viewModel.user.observe(viewLifecycleOwner) {
            if (usersMarker.isEmpty()) {
                for (value in 0 until it.size) {
                    val user = it[value]
                    val position = LatLng(user.location.latitude, user.location.longitude)
                    val marker = mMap.addMarker(
                        MarkerOptions()
                            .position(position)
                            .title(user.name)
                            .icon(
                                convertImage
                                    .convert(requireContext(), R.drawable.marker_other_user)
                            )
                    )
                    usersMarker.add(marker!!)
                }
            } else {
                for (value in 0 until it.size) {
                    val position = LatLng(it[value].location.latitude, it[value].location.longitude)
                    usersMarker[value].apply {
                        this.position = position
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        locUpdates.start(requireContext(), requireActivity())
    }

    override fun onStop() {
        super.onStop()

        locUpdates.stop()
    }
}