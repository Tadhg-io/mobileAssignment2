package ie.wit.pintmark.models

import android.net.Uri
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class MarkerModel(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var image: String = "",
    var lat : Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f,
    var category: String = "") : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable
