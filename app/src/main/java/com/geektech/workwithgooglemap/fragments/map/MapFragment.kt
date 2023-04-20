package com.geektech.workwithgooglemap.fragments.map

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.geektech.workwithgooglemap.R
import com.geektech.workwithgooglemap.fragments.MarkerTapListener
import com.geektech.workwithgooglemap.fragments.markers.MarkerAllUser
import com.geektech.workwithgooglemap.fragments.markers.MarkerThisUser
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_test_map), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var testMarkerThisUser = MarkerThisUser()
    private lateinit var testMarkerAllUser: MarkerAllUser
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
        //1
        uiScope.launch(Dispatchers.IO) {
            initialization()
        }
    }

    private fun initialization() {

        fusedLocClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        locRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 500)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(2000)
            .setMaxUpdateDelayMillis(800)
            .build()

        locThisUser()

        testMarkerAllUser = MarkerAllUser(requireContext(), requireActivity().applicationContext)
        locUpdates = MapLocUpdates(fusedLocClient, locRequest, locCallback)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        MarkerTapListener(googleMap).invoke()

        //2
        uiScope.launch {
            viewModel.listenUpdatesUsersLocation().collect {
                testMarkerAllUser.display(it, mMap)
            }
        }
        mMap.setOnCameraMoveStartedListener {
            uiScope.launch {
                val zoom = mMap.cameraPosition.zoom
                testMarkerAllUser.zoomChanged(zoom)
            }
        }
    }

    private fun locThisUser() {
        locCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                val location = locationResult.lastLocation!!
                Log.e("TAGGER", "onLocationResult: $location")
                if (mMap != null) {
                    //markerThisUser(locationResult.lastLocation!!)
                    testMarkerThisUser(location, mMap, requireActivity().applicationContext)
                    viewModel.updateThisUserLocation("marsel", location)
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