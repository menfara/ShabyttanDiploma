package farkhat.myrzabekov.shabyttan.presentation.usecase.user.favorites

import farkhat.myrzabekov.shabyttan.data.local.entity.UserFavoritesEntity
import farkhat.myrzabekov.shabyttan.domain.repository.UserFavoritesRepository
import javax.inject.Inject

class RemoveFromUserFavoritesUseCaseImpl @Inject constructor(private val userFavoritesRepository: UserFavoritesRepository) :
    RemoveFromUserFavoritesUseCase {

    override suspend fun invoke(userId: Long, artworkId: Long) {
        userFavoritesRepository.deleteUserFavorite(userId, artworkId)
    }
}
