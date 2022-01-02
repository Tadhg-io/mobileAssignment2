package ie.wit.pintmark.views.markerlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.Marker
import ie.wit.pintmark.R
import ie.wit.pintmark.views.marker.MarkerView
import ie.wit.pintmark.adapters.PintmarkAdapter
import ie.wit.pintmark.adapters.PintmarkListener
import ie.wit.pintmark.databinding.ActivityMarkerListBinding
import ie.wit.pintmark.main.MainApp
import ie.wit.pintmark.models.MarkerModel

class MarkerListView : AppCompatActivity(), PintmarkListener {

    lateinit var app: MainApp
    lateinit var binding: ActivityMarkerListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    lateinit var presenter: MarkerListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarkerListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initialise the presenter
        presenter = MarkerListPresenter(this)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = PintmarkAdapter(app.markers.findAll(),this)

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

    }

    override fun onMarkerClick(marker: MarkerModel) {
        presenter.openEditPlacemark(marker)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> { presenter.openAddPlacemark() }
            R.id.item_logout -> { presenter.doLogout() }
        }
        return super.onOptionsItemSelected(item)
    }

}
