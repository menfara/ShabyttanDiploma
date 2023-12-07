package farkhat.myrzabekov.shabyttan.presentation.usecase.user

import farkhat.myrzabekov.shabyttan.data.local.entity.UserEntity
import farkhat.myrzabekov.shabyttan.domain.repository.UserRepository
import javax.inject.Inject

class AuthorizeUserUseCaseImpl @Inject constructor(private val userRepository: UserRepository) :
    AuthorizeUserUseCase {

    override suspend fun invoke(username: String, password: String): Boolean {
        return userRepository.authorizeUser(username, password)
    }
}
