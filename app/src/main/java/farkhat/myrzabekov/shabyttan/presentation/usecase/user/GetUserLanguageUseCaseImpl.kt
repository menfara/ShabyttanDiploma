package farkhat.myrzabekov.shabyttan.presentation.usecase.user

import farkhat.myrzabekov.shabyttan.domain.repository.UserRepository
import javax.inject.Inject

class GetUserLanguageUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : GetUserLanguageUseCase {

    override suspend fun getUserLanguage(): String? {
        return userRepository.getUserLanguage()
    }
}