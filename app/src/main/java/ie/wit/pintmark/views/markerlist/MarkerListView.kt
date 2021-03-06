package ie.wit.pintmark.views.markerlist

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.Marker
import ie.wit.pintmark.R
import ie.wit.pintmark.views.marker.MarkerView
import ie.wit.pintmark.adapters.PintmarkAdapter
import ie.wit.pintmark.adapters.PintmarkListener
import ie.wit.pintmark.databinding.ActivityMarkerListBinding
import ie.wit.pintmark.helpers.OnSwipeTouchListener
import ie.wit.pintmark.main.MainApp
import ie.wit.pintmark.models.MarkerModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.text.style.ForegroundColorSpan

import android.text.SpannableString




class MarkerListView : AppCompatActivity(), PintmarkListener {

    lateinit var app: MainApp
    lateinit var binding: ActivityMarkerListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    lateinit var presenter: MarkerListPresenter

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarkerListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initialise the presenter
        presenter = MarkerListPresenter(this)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        // need a static reference of this activity
        val markerListActivity = this
        // load the markers from a coroutine
        GlobalScope.launch(Dispatchers.IO) {
            binding.recyclerView.adapter = PintmarkAdapter(app.markers.findAll(), markerListActivity)
        }

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

    }

    override fun onMarkerClick(marker: MarkerModel) {
        presenter.openEditPlacemark(marker)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val item = menu.getItem(1)
        val s = SpannableString("Logout")
        s.setSpan(ForegroundColorSpan(Color.BLACK), 0, s.length, 0)
        item.title = s
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
