package farkhat.myrzabekov.shabyttan.presentation.usecase.user

import farkhat.myrzabekov.shabyttan.data.local.entity.UserEntity
import farkhat.myrzabekov.shabyttan.domain.repository.UserRepository
import javax.inject.Inject

class CreateUserUseCaseImpl @Inject constructor(private val userRepository: UserRepository) :
    CreateUserUseCase {

    override suspend fun invoke(user: UserEntity) {
        userRepository.insertUser(user)
    }
}
