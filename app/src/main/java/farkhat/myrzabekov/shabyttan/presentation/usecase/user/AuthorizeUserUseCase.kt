package farkhat.myrzabekov.shabyttan.presentation.usecase.user

import farkhat.myrzabekov.shabyttan.data.local.entity.UserEntity

interface AuthorizeUserUseCase {
    suspend operator fun invoke(username: String, password: String): Boolean
}

