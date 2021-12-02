package com.unaj.loginsqlite

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.unaj.loginsqlite.model.Complex
import com.unaj.loginsqlite.sql.DatabaseHelper

class GoogleMapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
                            GoogleMap.OnInfoWindowClickListener{

    private lateinit var map: GoogleMap
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var locationFromIntent: String

    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_map)

        databaseHelper = DatabaseHelper(this)

        locationFromIntent = intent.getStringExtra("LOCATION") as String

        createMapFragment()
    }

    private fun createMapFragment () {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentMap) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        createMarker()
        map.setOnMarkerClickListener(this)
        map.setOnInfoWindowClickListener(this)
        enableMyLocation()
        zoomComplexFromIntent(this.locationFromIntent)

    }

    private fun createMarker() {
/*
        val complex1 = Complex(1,"Planeta Gol", "-34.7876912170861, -58.25886175164722", "42558965", 0, 1,1, "admin@gmail.com")
        val complex2 = Complex(2,"Sport 7", "-34.776372459147055, -58.25504980974949", "42554587", 1, 0,1, "admin2@gmail.com")
        val complex3 = Complex(3,"Mundo futbol", "-34.787644792529264, -58.2558092597688", "42544784", 1, 1,0, "admin3@gmail.com")

        val complexs = mutableListOf<Complex>()
        complexs.add(complex1)
        complexs.add(complex2)
        complexs.add(complex3)
        */



        val complexs = databaseHelper.getAllComplex()

        for (complex in complexs) {

            var loc = complex.location
            val delimiter = ","
            var locParts = loc.split(delimiter)

            map.addMarker(MarkerOptions().position(LatLng(locParts[0].toDouble(), locParts[1].toDouble()))
                .title(complex.name).snippet(getString(R.string.hint_phone) + ": " + complex.phone))
        }

    }

    private fun zoomComplexFromIntent(location: String) {
        var loc = location
        val delimiter = ","
        var locParts = loc.split(delimiter)

        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(LatLng(locParts[0].toDouble(), locParts[1].toDouble()), 18f),
            4000,
            null
        )
    }


    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (!::map.isInitialized) return
        if (isLocationPermissionGranted()) {
            map.isMyLocationEnabled = true
        } else {
            requestLocationPermission()
        }
    }


    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, getString(R.string.request_map_permission), Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION)
        }
    }

    @SuppressLint("MissingSuperCall", "MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode){
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                map.isMyLocationEnabled =true
            } else {
                Toast.makeText(this, getString(R.string.request_map_permission), Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    @SuppressLint("MissingPermission")
    override fun onResumeFragments() {
        super.onResumeFragments()
        if (!::map.isInitialized) return
        if (!isLocationPermissionGranted()){
            map.isMyLocationEnabled = false
            Toast.makeText(this, getString(R.string.request_map_permission), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        Toast.makeText(this, "Click en el marker", Toast.LENGTH_SHORT).show()

        return false
    }

    override fun onInfoWindowClick(p0: Marker) {
        Toast.makeText(this, "Click en ${p0.title}", Toast.LENGTH_SHORT).show()

    }
}