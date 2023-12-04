package farkhat.myrzabekov.shabyttan.data.repository

import android.content.SharedPreferences
import farkhat.myrzabekov.shabyttan.data.local.UserDao
import farkhat.myrzabekov.shabyttan.data.local.entity.UserEntity
import farkhat.myrzabekov.shabyttan.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val sharedPreferences: SharedPreferences
) : UserRepository {

    private val userId: Long
        get() = sharedPreferences.getLong(KEY_USER_ID, DEFAULT_USER_ID)

    override suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    override suspend fun getUserByUsername(username: String): UserEntity? {
        return userDao.getUserByUsername(username)
    }

    override suspend fun getUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }

    override suspend fun getUserId(): Long = userId

    override suspend fun setUserId(userId: Long) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putLong(KEY_USER_ID, userId).apply()
        }
    }

    override suspend fun setUserLanguage(language: String) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putString(KEY_LANGUAGE, language).apply()
        }
    }

    override suspend fun getUserLanguage(): String? {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getString(KEY_LANGUAGE, DEFAULT_LANGUAGE)
        }
    }

    override suspend fun getUserEmail(): String {
        return userDao.getUserEmail(userId)
    }

    override suspend fun getUserUsername(): String {
        return userDao.getUserUsername(userId)
    }

    companion object {
        private const val KEY_USER_ID = "userId"
        private const val DEFAULT_USER_ID = -1L
        private const val KEY_LANGUAGE = "language"
        private const val DEFAULT_LANGUAGE = "default"
    }
}

