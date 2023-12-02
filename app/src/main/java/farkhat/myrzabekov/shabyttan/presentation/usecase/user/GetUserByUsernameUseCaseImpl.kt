package farkhat.myrzabekov.shabyttan.presentation.usecase.user

import farkhat.myrzabekov.shabyttan.data.local.entity.UserEntity
import farkhat.myrzabekov.shabyttan.domain.repository.UserRepository
import javax.inject.Inject

class GetUserByUsernameUseCaseImpl @Inject constructor(private val userRepository: UserRepository) :
    GetUserByUsernameUseCase {

    override suspend fun invoke(username: String): UserEntity? {
        return userRepository.getUserByUsername(username)
    }
}
