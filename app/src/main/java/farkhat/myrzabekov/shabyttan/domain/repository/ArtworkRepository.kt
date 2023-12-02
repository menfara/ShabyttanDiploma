package farkhat.myrzabekov.shabyttan.domain.repository

import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity
import kotlinx.coroutines.flow.Flow

interface ArtworkRepository {

    suspend fun insertArtwork(artwork: ArtworkEntity)

    suspend fun getAllArtworks(): List<ArtworkEntity>

    suspend fun getArtworkById(artworkId: Long): ArtworkEntity?

    suspend fun getArtworkByViewDate(viewDate: String): ArtworkEntity?

    suspend fun getArtworksLikedByUser(userId: Long): Flow<List<ArtworkEntity>>

    suspend fun getArtworksByCreator(creator: String): List<ArtworkEntity>

    suspend fun searchArtworks(query: String): List<ArtworkEntity>

    suspend fun getRecommendedArtworks(): List<ArtworkEntity>

    suspend fun getLatestArtworks(): List<ArtworkEntity>


}
