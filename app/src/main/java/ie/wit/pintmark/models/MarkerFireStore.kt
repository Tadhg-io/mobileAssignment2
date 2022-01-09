package ie.wit.pintmark.models

import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ie.wit.pintmark.helpers.readImageFromPath
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File

class MarkerFireStore(val applicationContext: Context): MarkerStore {
    // local array of markers
    val markers = ArrayList<MarkerModel>()
    // for storing the logged in user
    lateinit var currentUser: String
    // reference to firebase storage
    lateinit var st: StorageReference
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
        updateImage(marker)
    }

    override suspend fun update(marker: MarkerModel) {
        var foundmarker: MarkerModel? = markers.find { p -> p.id == marker.id }
        if (foundmarker != null) {
            updateImage(marker)
            db.child("users").child(currentUser).child("markers").child(marker.id).setValue(marker)
        }
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
        st = FirebaseStorage.getInstance().reference
        db = FirebaseDatabase.getInstance("https://mobilea2-1f870-default-rtdb.europe-west1.firebasedatabase.app").reference
        markers.clear()
        db.child("users").child(currentUser).child("markers")
            .addListenerForSingleValueEvent(valueEventListener)
    }

    fun updateImage(marker: MarkerModel) {
        if (marker.image != "") {
            val fileName = File(marker.image)
            val imageName = fileName.getName()

            var imageRef = st.child(currentUser + '/' + imageName)
            val baos = ByteArrayOutputStream()
            val bitmap = readImageFromPath(applicationContext, marker.image)

            bitmap?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val uploadTask = imageRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    Timber.i("Failure: " + it.message)

                }.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        marker.image = it.toString()
                        db.child("users").child(currentUser).child("markers").child(marker.id).setValue(marker)
                    }
                }
            }
        }
    }
}