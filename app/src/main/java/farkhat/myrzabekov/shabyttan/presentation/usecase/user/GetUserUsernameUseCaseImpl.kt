package farkhat.myrzabekov.shabyttan.presentation.usecase.user

import farkhat.myrzabekov.shabyttan.data.local.entity.UserEntity
import farkhat.myrzabekov.shabyttan.domain.repository.UserRepository
import javax.inject.Inject

class GetUserUsernameUseCaseImpl @Inject constructor(private val userRepository: UserRepository) :
    GetUserUsernameUseCase {

    override suspend fun invoke(): String {
        return userRepository.getUserUsername()
    }
}
