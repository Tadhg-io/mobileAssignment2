package ie.wit.pintmark.main

import android.app.Application
import ie.wit.pintmark.models.MarkerJSONStore
import ie.wit.pintmark.models.MarkerStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    // variable to store the markers
    lateinit var markers: MarkerStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        markers = MarkerJSONStore(applicationContext)
        i("Main App started")
    }
}