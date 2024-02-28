package farkhat.myrzabekov.shabyttan.data.repository

import farkhat.myrzabekov.shabyttan.data.local.ArtworkDao
import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity
import farkhat.myrzabekov.shabyttan.domain.repository.ArtworkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArtworkRepositoryImpl @Inject constructor(private val artworkDao: ArtworkDao) :
    ArtworkRepository {

    override suspend fun insertArtwork(artwork: ArtworkEntity) {
        artworkDao.insertArtwork(artwork)
    }

    override suspend fun getAllArtworks(): List<ArtworkEntity> {
        return artworkDao.getAllArtworks()
    }

    override suspend fun getArtworkById(artworkId: Long): ArtworkEntity? {
        return artworkDao.getArtworkById(artworkId)
    }

    override suspend fun getArtworkByFirestoreId(artworkId: String): ArtworkEntity? {
        return artworkDao.getArtworkByFirestoreId(artworkId)
    }

    override suspend fun getArtworkByViewDate(): ArtworkEntity? {
        return artworkDao.getArtworkByViewDate()
    }

    override suspend fun getArtworksLikedByUser(userId: Long): Flow<List<ArtworkEntity>> {
        return artworkDao.getArtworksLikedByUser(userId)
    }

    override suspend fun getArtworksByCreator(creator: String): List<ArtworkEntity> {
        return artworkDao.getArtworksByCreator(creator)
    }

    override suspend fun searchArtworks(query: String): List<ArtworkEntity> {
        return artworkDao.searchArtworks(query)
    }

    override suspend fun getRecommendedArtworks(): List<ArtworkEntity> {
        return artworkDao.getRecommendedArtworks()
    }

    override suspend fun getLatestArtworks(): List<ArtworkEntity> {
        return artworkDao.getLatestArtworks()
    }



}