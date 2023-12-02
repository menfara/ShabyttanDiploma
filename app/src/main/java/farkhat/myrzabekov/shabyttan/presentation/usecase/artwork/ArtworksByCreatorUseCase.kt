package farkhat.myrzabekov.shabyttan.presentation.usecase.artwork

import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity

interface ArtworksByCreatorUseCase {
    suspend operator fun invoke(creator: String): List<ArtworkEntity>
}