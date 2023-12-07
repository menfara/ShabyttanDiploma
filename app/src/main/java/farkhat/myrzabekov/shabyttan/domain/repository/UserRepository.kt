package farkhat.myrzabekov.shabyttan.domain.repository

import farkhat.myrzabekov.shabyttan.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun insertUser(user: UserEntity): Long

    suspend fun getUserByUsername(username: String): UserEntity?

    suspend fun getUserByEmail(email: String): UserEntity?

    suspend fun getUserId(): Long

    suspend fun setUserId(userId: Long)

    suspend fun setUserLanguage(language: String)

    suspend fun getUserLanguage(): String?

    suspend fun getUserEmail(): String
    suspend fun getUserUsername(): String
    suspend fun authorizeUser(username: String, password: String): Boolean

}
