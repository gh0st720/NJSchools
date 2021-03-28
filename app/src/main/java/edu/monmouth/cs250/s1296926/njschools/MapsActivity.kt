package edu.monmouth.cs250.s1296926.njschools

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private val LOCATION_REQUEST_CODE = 101

    private var locationAccess = false
    private var zoomLevel = 10.0f

    private var njSchools = mutableListOf<School>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            locationAccess = true
            showMapFragment()
        }
        else {
            requestPermission( Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_REQUEST_CODE)
        }

        njSchools = School.getSchoolsFromFile("njschools.json",this)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun showMapFragment() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this) // permission granted. We can get the map now
    }

    private fun requestPermission(permissionType: String, requestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permissionType), requestCode )
    }
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray)
    {

        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                            this,
                            "Unable to show location - permission required",
                            Toast.LENGTH_LONG
                    ).show()
                } else {
                    // we have permissions. Enable location tracking and show map fragment
                    locationAccess = true
                    showMapFragment()
                }
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.mapmenu, menu)
        return true
    }
    


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // map settings properties

        val mapSettings = mMap.uiSettings
        mapSettings.isZoomControlsEnabled  = true
        mapSettings.isMapToolbarEnabled = true
        mapSettings.isCompassEnabled = true


        // Add a marker in Sydney and move the camera
        var muCampus = LatLng(40.2790893, -74.005459)
        mMap.addMarker(MarkerOptions().position(muCampus).title("Monmouth U").snippet("GO HAWKS!")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mu_icon_30)))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(muCampus, zoomLevel))

        showSchools(mMap)

    }
    private fun showSchools (mMap: GoogleMap) {
        for (school in njSchools) {

            val schoolLatLng = LatLng(school.lat, school.long)

            var parkMarker = mMap.addMarker(MarkerOptions().position(schoolLatLng).title(school.name).snippet(school.category)
                    .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
            parkMarker.tag = school.schoolID
        }

    }



    }


