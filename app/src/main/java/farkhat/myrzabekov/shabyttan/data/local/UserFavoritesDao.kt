package farkhat.myrzabekov.shabyttan.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import farkhat.myrzabekov.shabyttan.data.local.entity.UserFavoritesEntity

@Dao
interface UserFavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserFavorite(userFavorites: UserFavoritesEntity)

    @Query("DELETE FROM user_favorites WHERE userId = :userId AND artworkId = :artworkId")
    suspend fun deleteUserFavorite(userId: Long, artworkId: Long)

    @Query("SELECT * FROM user_favorites WHERE userId = :userId")
    suspend fun getUserFavorites(userId: Long): List<UserFavoritesEntity>

    @Query("SELECT COUNT(*) FROM user_favorites WHERE userId = :userId AND artworkId = :artworkId")
    suspend fun isArtworkInFavorites(userId: Long, artworkId: Long): Int

}
