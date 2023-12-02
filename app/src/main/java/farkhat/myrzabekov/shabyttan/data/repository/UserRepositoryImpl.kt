package farkhat.myrzabekov.shabyttan.data.repository

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import farkhat.myrzabekov.shabyttan.data.local.UserDao
import farkhat.myrzabekov.shabyttan.data.local.entity.UserEntity
import farkhat.myrzabekov.shabyttan.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val sharedPreferences: SharedPreferences
) : UserRepository {

    override suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    override suspend fun getUserByUsername(username: String): UserEntity? {
        return userDao.getUserByUsername(username)
    }

    override suspend fun getUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }

    override suspend fun getUserId(): Long {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getLong(
                SharedPreferencesKey.USER_ID.key,
                SharedPreferencesKey.USER_ID.defaultValue as Long
            )
        }
    }

    override suspend fun setUserId(userId: Long) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putLong(
                SharedPreferencesKey.USER_ID.key,
                userId
            ).apply()
        }
    }

    override suspend fun setUserLanguage(language: String) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putString(
                SharedPreferencesKey.LANGUAGE.key,
                language
            ).apply()
        }
    }

    override suspend fun getUserLanguage(): String? {
        return withContext(Dispatchers.IO) {
            sharedPreferences.getString(
                SharedPreferencesKey.LANGUAGE.key,
                SharedPreferencesKey.LANGUAGE.defaultValue as String
            )
        }
    }


    enum class SharedPreferencesKey(val key: String, val defaultValue: Any) {
        USER_ID("userId", -1L),
        LANGUAGE("language", "default")
    }

}

