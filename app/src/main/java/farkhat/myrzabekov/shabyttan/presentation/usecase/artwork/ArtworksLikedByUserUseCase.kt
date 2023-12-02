package farkhat.myrzabekov.shabyttan.presentation.usecase.artwork

import farkhat.myrzabekov.shabyttan.data.local.entity.ArtworkEntity
import kotlinx.coroutines.flow.Flow

interface ArtworksLikedByUserUseCase {
    suspend operator fun invoke(userId: Long): Flow<List<ArtworkEntity>>
}