package ie.wit.pintmark.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.wit.pintmark.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "placemarks.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<MarkerModel>>() {}.type

fun generateRandomId(): String {
    return Random().nextLong().toString()
}

class MarkerJSONStore(private val context: Context) : MarkerStore {

    var markers = mutableListOf<MarkerModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override suspend fun findById(id: String): MarkerModel? {
        val foundmarker: MarkerModel? = markers.find { p -> p.id == id }
        return foundmarker
    }

    override suspend fun findAll(): MutableList<MarkerModel> {
        logAll()
        return markers
    }

    override suspend fun create(marker: MarkerModel) {
        marker.id = generateRandomId()
        markers.add(marker)
        serialize()
    }


    override suspend fun update(marker: MarkerModel) {
        // get all markers in an array list
        val markerList = findAll() as ArrayList<MarkerModel>
        // find the marker to update by id
        var result: MarkerModel? = markerList.find { r -> r.id == marker.id }
        // overwrite the properties
        if (result != null) {
            result.title = marker.title
            result.description = marker.description
            result.category = marker.category
            result.image = marker.image
        }
        serialize()
    }

    override suspend fun delete (marker: MarkerModel) {
        // get all markers in an array list
        val markerList = findAll() as ArrayList<MarkerModel>
        // find the marker to delete by id
        var result: MarkerModel? = markerList.find { r -> r.id == marker.id }
        if (result != null) {
            // delete the marker
            markerList.remove(result)
        }
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(markers, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        markers = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        markers.forEach { Timber.i("$it") }
    }

    override suspend fun clear(){
        markers.clear()
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }

}