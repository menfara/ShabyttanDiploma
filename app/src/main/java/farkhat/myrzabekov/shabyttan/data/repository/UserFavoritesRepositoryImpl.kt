package farkhat.myrzabekov.shabyttan.data.repository

import farkhat.myrzabekov.shabyttan.data.local.UserFavoritesDao
import farkhat.myrzabekov.shabyttan.data.local.entity.UserFavoritesEntity
import farkhat.myrzabekov.shabyttan.domain.repository.UserFavoritesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserFavoritesRepositoryImpl @Inject constructor(private val userFavoritesDao: UserFavoritesDao) :
    UserFavoritesRepository {

    override suspend fun insertUserFavorite(userFavorite: UserFavoritesEntity) {
        userFavoritesDao.insertUserFavorite(userFavorite)
    }

    override suspend fun deleteUserFavorite(userId: Long, artworkId: Long) {
        userFavoritesDao.deleteUserFavorite(userId, artworkId)
    }

    override suspend fun getUserFavorites(userId: Long): List<UserFavoritesEntity> {
        return userFavoritesDao.getUserFavorites(userId)
    }

    override suspend fun isArtworkInFavorites(userId: Long, artworkId: Long): Boolean {
        val count = userFavoritesDao.isArtworkInFavorites(userId, artworkId)
        return count > 0
    }

}
