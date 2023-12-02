package farkhat.myrzabekov.shabyttan.presentation.usecase.artwork

import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity
import farkhat.myrzabekov.shabyttan.domain.repository.ArtworkRepository
import javax.inject.Inject

class GetArtworkByViewDateUseCaseImpl @Inject constructor(private val artworkRepository: ArtworkRepository) :
    GetArtworkByViewDateUseCase {

    override suspend fun invoke(viewDate: String): ArtworkEntity? {
        return artworkRepository.getArtworkByViewDate(viewDate)
    }
}
