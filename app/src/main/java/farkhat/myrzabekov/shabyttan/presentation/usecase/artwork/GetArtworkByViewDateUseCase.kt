package farkhat.myrzabekov.shabyttan.presentation.usecase.artwork

import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity

interface GetArtworkByViewDateUseCase {
    suspend operator fun invoke(viewDate: String): ArtworkEntity?
}
