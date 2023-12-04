package farkhat.myrzabekov.shabyttan.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtworkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtwork(artwork: ArtworkEntity)

    @Query("SELECT * FROM artworks")
    suspend fun getAllArtworks(): List<ArtworkEntity>

    @Query("SELECT * FROM artworks WHERE id = :artworkId")
    suspend fun getArtworkById(artworkId: Long): ArtworkEntity?

    @Query("SELECT * FROM artworks ORDER BY viewDate DESC LIMIT 1")
    suspend fun getArtworkByViewDate(): ArtworkEntity?

    @Query("SELECT * FROM artworks WHERE id IN (SELECT artworkId FROM user_favorites WHERE userId = :userId)")
    fun getArtworksLikedByUser(userId: Long): Flow<List<ArtworkEntity>>

    @Query("SELECT * FROM artworks WHERE creator = :creator")
    suspend fun getArtworksByCreator(creator: String): List<ArtworkEntity>

    @Query(
        "SELECT * FROM artworks WHERE " +
                "title LIKE '%' || :query || '%' OR  titleRu LIKE '%' || :query || '%' " +
                "OR creator LIKE '%' || :query || '%' OR creatorRu LIKE '%' || :query || '%'"
    )
    suspend fun searchArtworks(query: String): List<ArtworkEntity>

    @Query("SELECT * FROM artworks ORDER BY RANDOM() LIMIT 4")
    suspend fun getRecommendedArtworks(): List<ArtworkEntity>

    @Query("SELECT * FROM artworks ORDER BY id ASC LIMIT 3 OFFSET 1")
    suspend fun getLatestArtworks(): List<ArtworkEntity>

}