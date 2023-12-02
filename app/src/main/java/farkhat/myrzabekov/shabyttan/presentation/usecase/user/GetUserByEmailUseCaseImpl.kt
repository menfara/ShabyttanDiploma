package farkhat.myrzabekov.shabyttan.presentation.usecase.user

import farkhat.myrzabekov.shabyttan.data.local.entity.UserEntity
import farkhat.myrzabekov.shabyttan.domain.repository.UserRepository
import javax.inject.Inject

class GetUserByEmailUseCaseImpl @Inject constructor(private val userRepository: UserRepository) :
    GetUserByEmailUseCase {

    override suspend fun invoke(email: String): UserEntity? {
        return userRepository.getUserByEmail(email)
    }
}
