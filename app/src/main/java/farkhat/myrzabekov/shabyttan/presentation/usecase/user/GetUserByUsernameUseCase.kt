package farkhat.myrzabekov.shabyttan.presentation.usecase.user

import farkhat.myrzabekov.shabyttan.data.local.entity.UserEntity

interface GetUserByUsernameUseCase {
    suspend operator fun invoke(username: String): UserEntity?
}
