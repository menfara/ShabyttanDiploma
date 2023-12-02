package farkhat.myrzabekov.shabyttan.presentation.usecase.user

interface SetUserIdUseCase {

    suspend fun setUserId(userId: Long)

}