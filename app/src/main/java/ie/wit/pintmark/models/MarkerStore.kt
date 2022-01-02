package ie.wit.pintmark.models

interface MarkerStore {
    suspend fun findAll(): List<MarkerModel>
    suspend fun findById(id: Long): MarkerModel?
    suspend fun create(placemark: MarkerModel)
    suspend fun update(placemark: MarkerModel)
    suspend fun delete(placemark: MarkerModel)
}