package farkhat.myrzabekov.shabyttan.presentation.usecase.artwork

import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity

interface GetArtworkByIdUseCase {
    suspend operator fun invoke(artworkId: Long): ArtworkEntity?
}