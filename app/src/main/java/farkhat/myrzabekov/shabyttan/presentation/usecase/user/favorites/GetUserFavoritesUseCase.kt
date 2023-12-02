package farkhat.myrzabekov.shabyttan.presentation.usecase.user.favorites

import farkhat.myrzabekov.shabyttan.data.local.entity.UserFavoritesEntity

interface GetUserFavoritesUseCase {
    suspend operator fun invoke(userId: Long): List<UserFavoritesEntity>
}
