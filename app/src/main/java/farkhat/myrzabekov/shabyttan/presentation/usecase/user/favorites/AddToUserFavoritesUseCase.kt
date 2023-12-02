package farkhat.myrzabekov.shabyttan.presentation.usecase.user.favorites

import farkhat.myrzabekov.shabyttan.data.local.entity.UserFavoritesEntity

interface AddToUserFavoritesUseCase {
    suspend operator fun invoke(userFavorite: UserFavoritesEntity)
}
