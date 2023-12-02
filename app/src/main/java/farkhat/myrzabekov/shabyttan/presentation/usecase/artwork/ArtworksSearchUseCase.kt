package farkhat.myrzabekov.shabyttan.presentation.usecase.artwork

import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity

interface ArtworksSearchUseCase {
    suspend operator fun invoke(query: String): List<ArtworkEntity>
}