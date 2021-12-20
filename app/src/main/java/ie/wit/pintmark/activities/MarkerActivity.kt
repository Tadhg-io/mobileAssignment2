package ie.wit.pintmark.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.wit.pintmark.R
import ie.wit.pintmark.databinding.ActivityMarkerBinding
import ie.wit.pintmark.helpers.showImagePicker
import ie.wit.pintmark.main.MainApp
import ie.wit.pintmark.models.Location
import ie.wit.pintmark.models.MarkerModel
import timber.log.Timber.i


class MarkerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMarkerBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var marker = MarkerModel()
    lateinit var app : MainApp
    var edit = false
    var selectedCategory = "PUB"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarkerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }
        registerImagePickerCallback()
        registerMapCallback()

        app = application as MainApp
        i("Marker Activity started...")

        val categories = resources.getStringArray(R.array.categories)
        val spinner: Spinner = findViewById(R.id.category)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.categories,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                selectedCategory = categories[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // nothing selected
            }
        }

        if (intent.hasExtra("edit_marker")) {
            edit = true
            marker = intent.extras?.getParcelable("edit_marker")!!
            binding.markerTitle.setText(marker.title)
            binding.markerDescription.setText(marker.description)
            binding.btnAdd.setText(R.string.save_marker)
            binding.category.setSelection(categories.indexOf(marker.category.toString()))
            Picasso.get()
                .load(marker.image)
                .into(binding.image)
            if (marker.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_marker_image)
            }
        }

        binding.markerLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (marker.zoom != 0f) {
                location.lat =  marker.lat
                location.lng = marker.lng
                location.zoom = marker.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

        binding.btnAdd.setOnClickListener() {
            marker.title = binding.markerTitle.text.toString()
            marker.description = binding.markerDescription.text.toString()
            marker.category = selectedCategory
            if (marker.title.isEmpty()) {
                Snackbar.make(it,R.string.enter_marker_title, Snackbar.LENGTH_LONG)
                    .show()
            } else if (marker.title.length < 5) {
                Snackbar.make(it,R.string.length_marker_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.markers.update(marker.copy())
                } else {
                    app.markers.create(marker.copy())
                }
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_marker, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            marker.image = result.data!!.data!!
                            Picasso.get()
                                .load(marker.image)
                                .into(binding.image)
                            binding.chooseImage.setText(R.string.change_marker_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            marker.lat = location.lat
                            marker.lng = location.lng
                            marker.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

}