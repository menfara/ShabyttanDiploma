package farkhat.myrzabekov.shabyttan.domain.repository

import farkhat.myrzabekov.shabyttan.data.local.entity.UserFavoritesEntity

interface UserFavoritesRepository {

    suspend fun insertUserFavorite(userFavorite: UserFavoritesEntity)

    suspend fun deleteUserFavorite(userId: Long, artworkId: Long)

    suspend fun getUserFavorites(userId: Long): List<UserFavoritesEntity>

    suspend fun isArtworkInFavorites(userId: Long, artworkId: Long): Boolean

}
