package farkhat.myrzabekov.shabyttan.presentation.usecase.artwork

import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity
import farkhat.myrzabekov.shabyttan.domain.repository.ArtworkRepository
import javax.inject.Inject

class CreateArtworkUseCaseImpl @Inject constructor(private val artworkRepository: ArtworkRepository) :
    CreateArtworkUseCase {

    override suspend fun invoke(artwork: ArtworkEntity) {
        artworkRepository.insertArtwork(artwork)
    }
}
