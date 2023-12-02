package farkhat.myrzabekov.shabyttan.presentation.usecase.user

import farkhat.myrzabekov.shabyttan.domain.repository.UserRepository
import javax.inject.Inject

class SetUserIdUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : SetUserIdUseCase {

    override suspend fun setUserId(userId: Long) {
        userRepository.setUserId(userId)
    }
}
