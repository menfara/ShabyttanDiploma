package farkhat.myrzabekov.shabyttan.presentation.usecase.user

import farkhat.myrzabekov.shabyttan.domain.repository.UserRepository
import javax.inject.Inject

class GetUserIdUseCaseImpl @Inject constructor(private val userRepository: UserRepository) :
    GetUserIdUseCase {

    override suspend fun invoke(): Long {
        return userRepository.getUserId()
    }
}
