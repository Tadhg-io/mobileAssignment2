package ie.wit.pintmark.models

interface MarkerStore {
    fun findAll(): List<MarkerModel>
    fun create(placemark: MarkerModel)
    fun update(placemark: MarkerModel)
}