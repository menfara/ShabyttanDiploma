package farkhat.myrzabekov.shabyttan.presentation.usecase.user.favorites

import farkhat.myrzabekov.shabyttan.data.local.entity.UserFavoritesEntity
import farkhat.myrzabekov.shabyttan.domain.repository.UserFavoritesRepository
import javax.inject.Inject

class AddToUserFavoritesUseCaseImpl @Inject constructor(private val userFavoritesRepository: UserFavoritesRepository) :
    AddToUserFavoritesUseCase {

    override suspend fun invoke(userFavorite: UserFavoritesEntity) {
        userFavoritesRepository.insertUserFavorite(userFavorite)
    }
}
