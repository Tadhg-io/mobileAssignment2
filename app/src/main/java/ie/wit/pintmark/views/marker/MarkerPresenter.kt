package ie.wit.pintmark.views.marker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import com.squareup.picasso.Picasso
import ie.wit.pintmark.R
import ie.wit.pintmark.models.Location
import timber.log.Timber
import androidx.activity.result.ActivityResultLauncher
import ie.wit.pintmark.main.MainApp
import ie.wit.pintmark.models.MarkerModel
import ie.wit.pintmark.databinding.ActivityMarkerBinding
import ie.wit.pintmark.helpers.showImagePicker
import ie.wit.pintmark.views.map.MapView

class MarkerPresenter(val view: MarkerView) {

    var marker = MarkerModel()
    var app: MainApp = view.application as MainApp
    var binding: ActivityMarkerBinding = ActivityMarkerBinding.inflate(view.layoutInflater)
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var edit = false;

    // initialise
    init {
        if (view.intent.hasExtra("edit_marker")) {
            edit = true
            marker = view.intent.extras?.getParcelable("edit_marker")!!
        }
        registerImagePickerCallback()
        registerMapCallback()
    }

    suspend fun doSaveMarker(marker: MarkerModel) {
        if (edit) {
            app.markers.update(marker.copy())
        } else {
            app.markers.create(marker.copy())
        }
        view.setResult(AppCompatActivity.RESULT_OK)
        view.finish()
    }

    fun doCancel() {
        view.finish()
    }

    suspend fun doDeleteMarker() {
        app.markers.delete(view.marker.copy())
        view.setResult(AppCompatActivity.RESULT_OK)
        view.finish()
    }

    fun setImage() {
        showImagePicker(imageIntentLauncher)
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
                            marker.lat = location.lat
                            marker.lng = location.lng
                            marker.zoom = location.zoom
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            marker.image = result.data!!.data!!
                            Picasso.get()
                                .load(marker.image)
                                .into(binding.image)
                            binding.chooseImage.setText(R.string.change_marker_image)
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    fun setLocation() {
        val location = Location(52.245696, -7.139102, 15f)
        if (marker.zoom != 0f) {
            location.lat =  marker.lat
            location.lng = marker.lng
            location.zoom = marker.zoom
        }
        val launcherIntent = Intent(view, MapView::class.java)
            .putExtra("location", location)
        mapIntentLauncher.launch(launcherIntent)
    }

}