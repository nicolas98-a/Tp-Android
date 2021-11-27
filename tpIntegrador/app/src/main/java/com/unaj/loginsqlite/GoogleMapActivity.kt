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
import com.google.android.gms.maps.model.MarkerOptions
import com.unaj.loginsqlite.model.Complex
import com.unaj.loginsqlite.sql.DatabaseHelper

class GoogleMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var databaseHelper: DatabaseHelper

    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_map)

        databaseHelper = DatabaseHelper(this)

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
        enableMyLocation()

    }

    private fun createMarker() {

        val complex1 = Complex(1,"Planeta Gol", LatLng(-34.7876912170861, -58.25886175164722), "42558965", 0, 1,1, "admin@gmail.com")
       /* val complex1 = Complex(1,"Planeta Gol", LatLng(-34.7876912170861, -58.25886175164722), "42558965", 0, 1,1, "admin@gmail.com")
        val complex2 = Complex(2,"Sport 7", LatLng(-34.776372459147055, -58.25504980974949), "42554587", 1, 0,1, "admin2@gmail.com")
        val complex3 = Complex(3,"Mundo futbol", LatLng(-34.787644792529264, -58.2558092597688), "42544784", 1, 1,0, "admin3@gmail.com")

        val complexs = mutableListOf<Complex>()
        complexs.add(complex1)
        complexs.add(complex2)
        complexs.add(complex3)
                    */

        val complexs = databaseHelper.getAllComplex()

        for (complex in complexs) {
            map.addMarker(MarkerOptions().position(complex.location).title(complex.name))
        }


        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(complex1.location, 18f),
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
}