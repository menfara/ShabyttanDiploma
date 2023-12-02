package farkhat.myrzabekov.shabyttan.presentation.usecase.user.favorites

interface CheckArtworkInFavoritesUseCase {
    suspend operator fun invoke(userId: Long, artworkId: Long): Boolean
}