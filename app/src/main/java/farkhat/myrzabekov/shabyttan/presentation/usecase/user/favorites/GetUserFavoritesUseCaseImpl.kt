package farkhat.myrzabekov.shabyttan.presentation.usecase.user.favorites

import farkhat.myrzabekov.shabyttan.data.local.entity.UserFavoritesEntity
import farkhat.myrzabekov.shabyttan.domain.repository.UserFavoritesRepository
import javax.inject.Inject

class GetUserFavoritesUseCaseImpl @Inject constructor(private val userFavoritesRepository: UserFavoritesRepository) :
    GetUserFavoritesUseCase {

    override suspend fun invoke(userId: Long): List<UserFavoritesEntity> {
        return userFavoritesRepository.getUserFavorites(userId)
    }
}
