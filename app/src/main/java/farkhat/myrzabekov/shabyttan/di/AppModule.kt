package farkhat.myrzabekov.shabyttan.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import farkhat.myrzabekov.shabyttan.data.local.*
import farkhat.myrzabekov.shabyttan.data.repository.*
import farkhat.myrzabekov.shabyttan.domain.repository.*
import farkhat.myrzabekov.shabyttan.presentation.usecase.artwork.*
import farkhat.myrzabekov.shabyttan.presentation.usecase.user.*
import farkhat.myrzabekov.shabyttan.presentation.usecase.user.favorites.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    @Singleton
    fun provideArtworkDao(appDatabase: AppDatabase): ArtworkDao {
        return appDatabase.artworkDao()
    }

    @Provides
    @Singleton
    fun provideUserFavoritesDao(appDatabase: AppDatabase): UserFavoritesDao {
        return appDatabase.userFavoritesDao()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        userDao: UserDao,
        sharedPreferences: SharedPreferences
    ): UserRepository {
        return UserRepositoryImpl(userDao, sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideArtworkRepository(artworkDao: ArtworkDao): ArtworkRepository {
        return ArtworkRepositoryImpl(artworkDao)
    }

    @Provides
    @Singleton
    fun provideUserFavoritesRepository(userFavoritesDao: UserFavoritesDao): UserFavoritesRepository {
        return UserFavoritesRepositoryImpl(userFavoritesDao)
    }

    @Provides
    @Singleton
    fun provideCreateUserUseCase(userRepository: UserRepository): CreateUserUseCase {
        return CreateUserUseCaseImpl(userRepository)
    }

    @Provides
    @Singleton
    fun provideGetUserByUsernameUseCase(userRepository: UserRepository): GetUserByUsernameUseCase {
        return GetUserByUsernameUseCaseImpl(userRepository)
    }

    @Provides
    @Singleton
    fun provideGetUserByEmailUseCase(userRepository: UserRepository): GetUserByEmailUseCase {
        return GetUserByEmailUseCaseImpl(userRepository)
    }

    @Provides
    @Singleton
    fun provideCreateArtworkUseCase(artworkRepository: ArtworkRepository): CreateArtworkUseCase {
        return CreateArtworkUseCaseImpl(artworkRepository)
    }

    @Provides
    @Singleton
    fun provideGetAllArtworksUseCase(artworkRepository: ArtworkRepository): GetAllArtworksUseCase {
        return GetAllArtworksUseCaseImpl(artworkRepository)
    }

    @Provides
    @Singleton
    fun provideGetArtworkByIdUseCase(artworkRepository: ArtworkRepository): GetArtworkByIdUseCase {
        return GetArtworkByIdUseCaseImpl(artworkRepository)
    }

    @Provides
    @Singleton
    fun provideGetArtworkByViewDateUseCase(artworkRepository: ArtworkRepository): GetArtworkByViewDateUseCase {
        return GetArtworkByViewDateUseCaseImpl(artworkRepository)
    }

    @Provides
    @Singleton
    fun provideAddToUserFavoritesUseCase(userFavoritesRepository: UserFavoritesRepository): AddToUserFavoritesUseCase {
        return AddToUserFavoritesUseCaseImpl(userFavoritesRepository)
    }

    @Provides
    @Singleton
    fun provideRemoveFromUserFavoritesUseCase(userFavoritesRepository: UserFavoritesRepository): RemoveFromUserFavoritesUseCase {
        return RemoveFromUserFavoritesUseCaseImpl(userFavoritesRepository)
    }

    @Provides
    @Singleton
    fun provideGetUserFavoritesUseCase(userFavoritesRepository: UserFavoritesRepository): GetUserFavoritesUseCase {
        return GetUserFavoritesUseCaseImpl(userFavoritesRepository)
    }

    @Provides
    @Singleton
    fun provideCheckArtworkInFavoritesUseCase(userFavoritesRepository: UserFavoritesRepository): CheckArtworkInFavoritesUseCase {
        return CheckArtworkInFavoritesUseCaseImpl(userFavoritesRepository)
    }


    @Provides
    @Singleton
    fun provideArtworksLikedByUserUseCase(artworkRepository: ArtworkRepository): ArtworksLikedByUserUseCase {
        return ArtworksLikedByUserUseCaseImpl(artworkRepository)
    }

    @Provides
    @Singleton
    fun provideArtworksByCreatorUseCase(artworkRepository: ArtworkRepository): ArtworksByCreatorUseCase {
        return ArtworksByCreatorUseCaseImpl(artworkRepository)
    }

    @Provides
    @Singleton
    fun provideArtworksSearchUseCase(artworkRepository: ArtworkRepository): ArtworksSearchUseCase {
        return ArtworksSearchUseCaseImpl(artworkRepository)
    }

    @Provides
    @Singleton
    fun provideGetUserIdUseCase(userRepository: UserRepository): GetUserIdUseCase {
        return GetUserIdUseCaseImpl(userRepository)
    }

    @Provides
    @Singleton
    fun provideSetUserIdUseCase(userRepository: UserRepository): SetUserIdUseCase {
        return SetUserIdUseCaseImpl(userRepository)
    }

    @Provides
    @Singleton
    fun provideGetLanguageUseCase(userRepository: UserRepository): GetUserLanguageUseCase {
        return GetUserLanguageUseCaseImpl(userRepository)
    }

    @Provides
    @Singleton
    fun provideSetLanguageUseCase(userRepository: UserRepository): SetUserLanguageUseCase {
        return SetUserLanguageUseCaseImpl(userRepository)
    }

    @Provides
    @Singleton
    fun provideGetRecommendedArtworksUseCase(
        artworkRepository: ArtworkRepository
    ): GetRecommendedArtworksUseCase {
        return GetRecommendedArtworksUseCaseImpl(artworkRepository)
    }

    @Provides
    @Singleton
    fun provideGetLatestArtworksUseCase(
        artworkRepository: ArtworkRepository
    ): GetLatestArtworksUseCase {
        return GetLatestArtworksUseCaseImpl(artworkRepository)
    }

    @Provides
    @Singleton
    fun provideUserEmailUseCase(
        userRepository: UserRepository
    ): GetUserEmailUseCase {
        return GetUserEmailUseCaseImpl(userRepository)
    }

    @Provides
    @Singleton
    fun provideUserUsernameUseCase(
        userRepository: UserRepository
    ): GetUserUsernameUseCase {
        return GetUserUsernameUseCaseImpl(userRepository)
    }

    @Provides
    @Singleton
    fun provideAuthorizeUserUseCase(
        userRepository: UserRepository
    ): AuthorizeUserUseCase {
        return AuthorizeUserUseCaseImpl(userRepository)
    }
}
