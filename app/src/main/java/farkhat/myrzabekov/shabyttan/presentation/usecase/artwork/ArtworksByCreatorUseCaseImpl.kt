package farkhat.myrzabekov.shabyttan.presentation.usecase.artwork

import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity
import farkhat.myrzabekov.shabyttan.domain.repository.ArtworkRepository
import javax.inject.Inject

class ArtworksByCreatorUseCaseImpl @Inject constructor(private val artworkRepository: ArtworkRepository) :
    ArtworksByCreatorUseCase {

    override suspend fun invoke(creator: String): List<ArtworkEntity> {
        return artworkRepository.getArtworksByCreator(creator)
    }
}