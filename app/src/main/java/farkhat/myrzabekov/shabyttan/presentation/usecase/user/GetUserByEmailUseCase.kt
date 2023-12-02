package farkhat.myrzabekov.shabyttan.presentation.usecase.user

import farkhat.myrzabekov.shabyttan.data.local.entity.UserEntity

interface GetUserByEmailUseCase {
    suspend operator fun invoke(email: String): UserEntity?
}

