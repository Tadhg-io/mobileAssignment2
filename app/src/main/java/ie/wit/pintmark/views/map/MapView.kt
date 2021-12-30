package ie.wit.pintmark.views.map

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.pintmark.R
import ie.wit.pintmark.databinding.ActivityMapBinding
import ie.wit.pintmark.models.Location
import ie.wit.pintmark.views.map.MapPresenter

class MapView : AppCompatActivity(),
    OnMapReadyCallback,
    GoogleMap.OnMarkerDragListener,
    GoogleMap.OnMarkerClickListener {

    lateinit var presenter: MapPresenter
    private lateinit var map: GoogleMap
    var location = Location()

    override fun onCreate(savedInstanceState: Bundle?) {
        presenter = MapPresenter(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        location = intent.extras?.getParcelable<Location>("location")!!
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        presenter.initialiseMap(map)
    }

    override fun onMarkerDragStart(marker: Marker) { // presenter

    }

    override fun onMarkerDrag(marker: Marker) {

    }

    override fun onMarkerDragEnd(marker: Marker) {
        presenter.updateLocation(marker.position.latitude, marker.position.longitude, map.cameraPosition.zoom)
    }

    override fun onBackPressed() {
        presenter.goBack()
        super.onBackPressed()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        presenter.updateMarkerLocation(marker)
        return false
    }


}
