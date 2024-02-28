package farkhat.myrzabekov.shabyttan.presentation.usecase.firestore

import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity
import farkhat.myrzabekov.shabyttan.domain.repository.FirestoreRepository
import javax.inject.Inject

class GetArtworkByIdUseFirestoreCaseImpl @Inject constructor(private val firestoreRepository: FirestoreRepository) :
    GetArtworkByIdFirestoreUseCase {

    override suspend fun invoke(artworkId: String): ArtworkEntity? {
        return firestoreRepository.getArtworkById(artworkId)
    }
}