package ie.wit.pintmark.room

import androidx.room.*
import ie.wit.pintmark.models.MarkerModel

@Dao
interface MarkerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(placemark: MarkerModel)

    @Query("SELECT * FROM MarkerModel")
    suspend fun findAll(): List<MarkerModel>

    @Query("select * from MarkerModel where id = :id")
    suspend fun findById(id: Long): MarkerModel

    @Update
    suspend fun update(placemark: MarkerModel)

    @Delete
    suspend fun deleteMarker(placemark: MarkerModel)
}