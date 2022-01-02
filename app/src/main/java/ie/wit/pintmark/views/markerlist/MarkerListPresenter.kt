package ie.wit.pintmark.views.markerlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.maps.model.Marker
import ie.wit.pintmark.R
import ie.wit.pintmark.views.marker.MarkerView
import ie.wit.pintmark.adapters.PintmarkListener
import ie.wit.pintmark.main.MainApp
import ie.wit.pintmark.models.MarkerModel
import ie.wit.pintmark.views.account.login.LoginView
import ie.wit.pintmark.views.marker.MarkerPresenter

class MarkerListPresenter(val view : MarkerListView) {

    var app: MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    init {
        app = view.application as MainApp
        registerRefreshCallback()
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { view.binding.recyclerView.adapter?.notifyDataSetChanged() }
    }

    fun openAddPlacemark() {
        val launcherIntent = Intent(view, MarkerView::class.java)
        refreshIntentLauncher.launch(launcherIntent)
    }

    fun doLogout(){
        val launcherIntent = Intent(view, LoginView::class.java)
        refreshIntentLauncher.launch(launcherIntent)
    }

    fun openEditPlacemark(marker: MarkerModel) {
        val launcherIntent = Intent(view, MarkerView::class.java)
        launcherIntent.putExtra("edit_marker", marker)
        refreshIntentLauncher.launch(launcherIntent)
    }

    suspend fun getMarkers() = app.markers.findAll()

}