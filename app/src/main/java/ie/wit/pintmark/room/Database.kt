package ie.wit.pintmark.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ie.wit.pintmark.helpers.Converters
import ie.wit.pintmark.models.MarkerModel

@Database(entities = arrayOf(MarkerModel::class), version = 1,  exportSchema = false)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {

    abstract fun markerDao(): MarkerDao
}