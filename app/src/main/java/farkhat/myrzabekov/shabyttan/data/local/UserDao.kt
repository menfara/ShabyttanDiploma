package farkhat.myrzabekov.shabyttan.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import farkhat.myrzabekov.shabyttan.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT email FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserEmail(userId: Long): String

    @Query("SELECT username FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserUsername(userId: Long): String

}