package ie.wit.pintmark.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.pintmark.R
import ie.wit.pintmark.adapters.PintmarkAdapter
import ie.wit.pintmark.adapters.PintmarkListener
import ie.wit.pintmark.databinding.ActivityMarkerListBinding
import ie.wit.pintmark.main.MainApp
import ie.wit.pintmark.models.MarkerModel

class MarkerListActivity : AppCompatActivity(), PintmarkListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityMarkerListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarkerListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = PintmarkAdapter(app.markers.findAll(),this)

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        registerRefreshCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, MarkerActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMarkerClick(marker: MarkerModel) {
        val launcherIntent = Intent(this, MarkerActivity::class.java)
        launcherIntent.putExtra("edit_marker", marker)
        refreshIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { binding.recyclerView.adapter?.notifyDataSetChanged() }
    }
}
