package ie.wit.pintmark.models

interface MarkerStore {
    suspend fun findById(id: String): MarkerModel?
    suspend fun findAll(): List<MarkerModel>
    suspend fun create(placemark: MarkerModel)
    suspend fun update(placemark: MarkerModel)
    suspend fun delete(placemark: MarkerModel)
    suspend fun clear()
}