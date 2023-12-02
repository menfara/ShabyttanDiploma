package farkhat.myrzabekov.shabyttan.presentation.usecase.artwork

import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity
import farkhat.myrzabekov.shabyttan.domain.repository.ArtworkRepository
import javax.inject.Inject

class GetArtworkByIdUseCaseImpl @Inject constructor(private val artworkRepository: ArtworkRepository) :
    GetArtworkByIdUseCase {

    override suspend fun invoke(artworkId: Long): ArtworkEntity? {
        return artworkRepository.getArtworkById(artworkId)
    }
}
