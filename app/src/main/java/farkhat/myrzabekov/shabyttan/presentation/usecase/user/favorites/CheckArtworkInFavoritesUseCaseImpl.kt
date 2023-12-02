package farkhat.myrzabekov.shabyttan.presentation.usecase.user.favorites

import farkhat.myrzabekov.shabyttan.domain.repository.UserFavoritesRepository
import javax.inject.Inject

class CheckArtworkInFavoritesUseCaseImpl @Inject constructor(
    private val userFavoritesRepository: UserFavoritesRepository
) : CheckArtworkInFavoritesUseCase {

    override suspend fun invoke(userId: Long, artworkId: Long): Boolean {
        return userFavoritesRepository.isArtworkInFavorites(userId, artworkId)
    }
}