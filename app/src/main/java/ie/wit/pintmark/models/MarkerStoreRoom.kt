package ie.wit.pintmark.models

import android.content.Context
import androidx.room.Room
import ie.wit.pintmark.models.MarkerModel
import ie.wit.pintmark.models.MarkerStore
import ie.wit.pintmark.room.Database
import ie.wit.pintmark.room.MarkerDao

class MarkerStoreRoom(val context: Context) : MarkerStore {

    var dao: MarkerDao

    init {
        val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
            .fallbackToDestructiveMigration()
            .build()
        dao = database.markerDao()
    }

    override suspend fun findAll(): List<MarkerModel> {
        return dao.findAll()
    }

    override suspend fun findById(id: Long): MarkerModel? {
        return dao.findById(id)
    }

    override suspend fun create(placemark: MarkerModel) {
        dao.create(placemark)
    }

    override suspend fun update(placemark: MarkerModel) {
        dao.update(placemark)
    }

    override suspend fun delete(placemark: MarkerModel) {
        dao.deleteMarker(placemark)
    }

    fun clear() {
    }
}