package farkhat.myrzabekov.shabyttan.presentation.usecase.firestore

import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity

interface AddArtworkFirestoreUseCase {
    suspend operator fun invoke(artwork: ArtworkEntity)
}
