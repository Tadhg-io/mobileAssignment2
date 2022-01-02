package ie.wit.pintmark.views.marker

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
import ie.wit.pintmark.views.map.MapView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber.i


class MarkerView : AppCompatActivity() {

    private lateinit var binding: ActivityMarkerBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var presenter : MarkerPresenter
    var marker = MarkerModel()
    lateinit var app : MainApp
    var edit = false
    var selectedCategory = "PUB"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarkerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = MarkerPresenter(this)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        // IMAGE BUTTON
        binding.chooseImage.setOnClickListener {
            presenter.setImage()
        }

        val categories = resources.getStringArray(R.array.categories)

        app = application as MainApp
        i("Marker Activity started...")

        // IF EDIT
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
        // IF CREATE
        else {
            binding.btnDelete.setVisibility(View.GONE)
        }

        // CATEGORY SPINNER
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

        // LOCATION BUTTON
        binding.markerLocation.setOnClickListener {
            presenter.setLocation()
        }

        // ADD BUTTON
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
                GlobalScope.launch(Dispatchers.IO) {
                    presenter.doSaveMarker(marker)
                }
            }
        }

        // DELETE BUTTON
        binding.btnDelete.setOnClickListener() {
            GlobalScope.launch(Dispatchers.IO) {
                presenter.doDeleteMarker()
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
                presenter.doCancel()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}