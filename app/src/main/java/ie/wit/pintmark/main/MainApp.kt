package ie.wit.pintmark.main

import android.app.Application
import ie.wit.pintmark.models.MarkerFireStore
import ie.wit.pintmark.models.MarkerJSONStore
import ie.wit.pintmark.models.MarkerStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    // variable to store the markers
    lateinit var markers: MarkerStore

    override fun onCreate() {
        // call the superclass method
        super.onCreate()
        // for debug logging
        Timber.plant(Timber.DebugTree())
        // create the Firebase object for ftetching data
        markers = MarkerFireStore(applicationContext)

        i("Main App started")
    }
}