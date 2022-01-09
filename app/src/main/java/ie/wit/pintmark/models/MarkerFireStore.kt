package ie.wit.pintmark.models

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MarkerFireStore(val applicationContext: Context): MarkerStore {
    // local array of markers
    val markers = ArrayList<MarkerModel>()
    // for storing the logged in user
    lateinit var currentUser: String
    // a reference to our firebase RealtimeDB
    lateinit var db: DatabaseReference

    override suspend fun findAll(): List<MarkerModel> {
        return markers
    }

    override suspend fun findById(id: String): MarkerModel? {
        val foundMarker: MarkerModel? = markers.find { p -> p.id == id }
        return foundMarker
    }

    override suspend fun create(marker: MarkerModel) {
        val key = db.child("users").child(currentUser).child("markers").push().key
        key?.let {
            marker.id = key
            markers.add(marker)
            db.child("users").child(currentUser).child("markers").child(key).setValue(marker)
        }
    }

    override suspend fun update(marker: MarkerModel) {
        var foundPlacemark: MarkerModel? = markers.find { p -> p.id == marker.id }
        if (foundPlacemark != null) {
            foundPlacemark.title = marker.title
            foundPlacemark.description = marker.description
            foundPlacemark.image = marker.image
            foundPlacemark.lat = marker.lat
            foundPlacemark.lng = marker.lng
        }

        db.child("users").child(currentUser).child("markers").child(marker.id).setValue(marker)

    }

    override suspend fun delete(marker: MarkerModel) {
        db.child("users").child(currentUser).child("markers").child(marker.id).removeValue()
        markers.remove(marker)
    }

    override suspend fun clear() {
        markers.clear()
    }

    fun fetchMarkers(markersReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(markers) {
                    it.getValue(
                        MarkerModel::class.java
                    )
                }
                markersReady()
            }
        }
        currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance("https://mobilea2-1f870-default-rtdb.europe-west1.firebasedatabase.app").reference
        markers.clear()
        db.child("users").child(currentUser).child("markers")
            .addListenerForSingleValueEvent(valueEventListener)
    }
}