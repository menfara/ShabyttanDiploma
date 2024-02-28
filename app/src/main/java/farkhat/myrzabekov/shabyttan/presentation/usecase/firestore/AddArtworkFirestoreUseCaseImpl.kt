package farkhat.myrzabekov.shabyttan.presentation.usecase.firestore

import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity
import farkhat.myrzabekov.shabyttan.domain.repository.FirestoreRepository
import javax.inject.Inject

class AddArtworkFirestoreUseCaseImpl @Inject constructor(private val firestoreRepository: FirestoreRepository) :
    AddArtworkFirestoreUseCase {

    override suspend fun invoke(artwork: ArtworkEntity) {
        firestoreRepository.addArtwork(artwork)
    }
}