package com.example.baladeyti.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.baladeyti.R
import com.example.baladeyti.fragments.FavouritesFragment
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point

import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource


class MapActivity : AppCompatActivity(),PermissionsListener , OnMapReadyCallback ,
    MapboxMap.OnMapClickListener {

    lateinit var mSharedPref: SharedPreferences
    private var locationComponent: LocationComponent? = null
    private var permissionsManager: PermissionsManager = PermissionsManager(this)
    private lateinit var mapboxMap: MapboxMap
    private lateinit var button: Button
    private lateinit var mapView: MapView
    var positionGps : MutableList<Double>? = ArrayList()
    private var destinationMarker : Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

// Mapbox access token is configured here. This needs to be called either in your application
// object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.access_token))



// This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_map)


        supportActionBar?.hide()


        mSharedPref = getSharedPreferences("UserPref", MODE_PRIVATE)
        mapView = findViewById(R.id.mapView)
        button = findViewById(R.id.button)
        /* mapView.onCreate(savedInstanceState)*/
        mapView.getMapAsync{
            mapboxMap = it
            mapboxMap.addOnMapClickListener (this)
            mapboxMap.setStyle(Style.OUTDOORS) {

// Map is set up and the style has loaded. Now you can add data or make other map adjustments
                enableLocationComponent(it)
            }
        }

        button.setOnClickListener {
            if(positionGps!!.isNotEmpty()){
                mSharedPref.edit().apply{
                    intent.putExtra("lat",positionGps!![0].toString())
                    intent.putExtra("long",positionGps!![1].toString())
                    putString("lat", positionGps!![0].toString())
                    putString("long", positionGps!![1].toString())
                }.apply()
            }
            println("position envoyé  est : "+mSharedPref.getString("lat",""))
            println("position envoyé  est : "+mSharedPref.getString("long",""))
            finish()
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap

        mapboxMap.setStyle(Style.OUTDOORS) {

// Map is set up and the style has loaded. Now you can add data or make other map adjustments
            enableLocationComponent(it)
        }
    }

    override fun onMapClick(point: LatLng): Boolean {
        mapboxMap.clear()
        positionGps!!.clear()
        destinationMarker = mapboxMap.addMarker(MarkerOptions().position(point))
        positionGps!!.add(point.latitude)
        positionGps!!.add(point.longitude)
        println("position selectionn est : "+point.toString())
        return true
    }



    @SuppressLint("MissingPermission")
    private fun enableLocationComponent(loadedMapStyle: Style) {
// Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

// Create and customize the LocationComponent's options
            val customLocationComponentOptions = LocationComponentOptions.builder(this)
                .trackingGesturesManagement(true)
                .accuracyColor(ContextCompat.getColor(this, R.color.primary))
                .build()

            val locationComponentActivationOptions = LocationComponentActivationOptions.builder(this, loadedMapStyle)
                .locationComponentOptions(customLocationComponentOptions)
                .build()

// Get an instance of the LocationComponent and then adjust its settings
            mapboxMap.locationComponent.apply {


// Activate the LocationComponent with options
                activateLocationComponent(locationComponentActivationOptions)

// Enable to make the LocationComponent visible
                isLocationComponentEnabled = true


// Set the LocationComponent's camera mode
                cameraMode = CameraMode.TRACKING

// Set the LocationComponent's render mode
                renderMode = RenderMode.COMPASS

            }
            println(mapboxMap.locationComponent.locationEngine.toString() + "here is a location !!!!!!!!!!!")
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onExplanationNeeded(permissionsToExplain: List<String>) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            enableLocationComponent(mapboxMap.style!!)
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}