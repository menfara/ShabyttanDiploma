package farkhat.myrzabekov.shabyttan.presentation.usecase.user.favorites


interface RemoveFromUserFavoritesUseCase {
    suspend operator fun invoke(userId: Long, artworkId: Long)
}
