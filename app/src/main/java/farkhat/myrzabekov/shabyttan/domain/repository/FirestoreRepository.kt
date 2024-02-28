package farkhat.myrzabekov.shabyttan.domain.repository

import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity

interface FirestoreRepository {
    fun addArtwork(artwork: ArtworkEntity)
    suspend fun getArtworkById(artworkId: String): ArtworkEntity?
}