package farkhat.myrzabekov.shabyttan.presentation.usecase.user

import farkhat.myrzabekov.shabyttan.data.local.entity.UserEntity

interface GetUserUsernameUseCase {
    suspend operator fun invoke(): String
}

