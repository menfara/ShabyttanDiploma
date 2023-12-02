package farkhat.myrzabekov.shabyttan.presentation.usecase.user

import farkhat.myrzabekov.shabyttan.data.local.entity.UserEntity

interface CreateUserUseCase {
    suspend operator fun invoke(user: UserEntity)
}
