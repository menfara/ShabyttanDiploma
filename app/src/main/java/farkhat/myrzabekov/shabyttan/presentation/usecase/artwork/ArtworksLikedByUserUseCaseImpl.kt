package farkhat.myrzabekov.shabyttan.presentation.usecase.artwork

import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity
import farkhat.myrzabekov.shabyttan.domain.repository.ArtworkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArtworksLikedByUserUseCaseImpl @Inject constructor(private val artworkRepository: ArtworkRepository) :
    ArtworksLikedByUserUseCase {

    override suspend fun invoke(userId: Long): Flow<List<ArtworkEntity>> {
        return artworkRepository.getArtworksLikedByUser(userId)
    }
}