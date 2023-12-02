package farkhat.myrzabekov.shabyttan.presentation.usecase.artwork

import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity
import farkhat.myrzabekov.shabyttan.domain.repository.ArtworkRepository
import javax.inject.Inject

class GetLatestArtworksUseCaseImpl @Inject constructor(private val artworkRepository: ArtworkRepository) :
    GetLatestArtworksUseCase {

    override suspend fun invoke(): List<ArtworkEntity> {
        return artworkRepository.getLatestArtworks()
    }
}