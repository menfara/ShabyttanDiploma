package farkhat.myrzabekov.shabyttan.presentation.usecase.artwork

import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity

interface GetAllArtworksUseCase {
    suspend operator fun invoke(): List<ArtworkEntity>
}
