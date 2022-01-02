package ie.wit.pintmark.models

import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class MarkerModel(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var title: String = "",
    var description: String = "",
    var image: Uri = Uri.EMPTY,
    var lat : Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f,
    var category: String = "") : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable
