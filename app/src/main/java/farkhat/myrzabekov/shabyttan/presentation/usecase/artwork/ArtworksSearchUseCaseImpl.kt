package farkhat.myrzabekov.shabyttan.presentation.usecase.artwork

import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity
import farkhat.myrzabekov.shabyttan.domain.repository.ArtworkRepository
import javax.inject.Inject

class ArtworksSearchUseCaseImpl @Inject constructor(private val artworkRepository: ArtworkRepository) :
    ArtworksSearchUseCase {

    override suspend fun invoke(query: String): List<ArtworkEntity> {
        return artworkRepository.searchArtworks(query)
    }
}