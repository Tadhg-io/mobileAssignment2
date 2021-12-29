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

class MapPresenter (val view: MapView) {

    var location = Location()

    // initialise
    init {
        location = view.intent.extras?.getParcelable<Location>("location")!!
    }

    fun initialiseMap (map: GoogleMap) {
        val loc = LatLng(location.lat, location.lng)
        val options = MarkerOptions()
            .title("Marker")
            .snippet("GPS : $loc")
            .draggable(true)
            .position(loc)
        map.addMarker(options)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
    }

    fun updateLocation(latitude: Double, longitude: Double, zoom: Float) {
        location.lat = latitude
        location.lng = longitude
        location.zoom = zoom
    }

    fun goBack() {
        val resultIntent = Intent()
        resultIntent.putExtra("location", location)
        view.setResult(AppCompatActivity.RESULT_OK, resultIntent)
        view.finish()
    }

    fun updateMarkerLocation(marker: Marker) {
        val loc = LatLng(location.lat, location.lng)
        marker.snippet = "GPS : $loc"
    }
}