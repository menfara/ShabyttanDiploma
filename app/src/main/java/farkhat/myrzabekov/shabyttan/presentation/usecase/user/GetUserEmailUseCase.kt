package farkhat.myrzabekov.shabyttan.presentation.usecase.user

import farkhat.myrzabekov.shabyttan.data.local.entity.UserEntity

interface GetUserEmailUseCase {
    suspend operator fun invoke(): String
}

