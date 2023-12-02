package farkhat.myrzabekov.shabyttan.presentation.usecase.artwork

import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity

interface CreateArtworkUseCase {
    suspend operator fun invoke(artwork: ArtworkEntity)
}
