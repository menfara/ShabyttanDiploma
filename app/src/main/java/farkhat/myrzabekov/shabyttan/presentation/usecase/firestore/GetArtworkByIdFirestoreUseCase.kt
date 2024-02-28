package farkhat.myrzabekov.shabyttan.presentation.usecase.firestore

import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity

interface GetArtworkByIdFirestoreUseCase {
    suspend operator fun invoke(artworkId: String): ArtworkEntity?
}