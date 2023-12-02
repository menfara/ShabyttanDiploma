package farkhat.myrzabekov.shabyttan.presentation.usecase.user

import farkhat.myrzabekov.shabyttan.domain.repository.UserRepository
import javax.inject.Inject

class SetUserLanguageUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : SetUserLanguageUseCase {

    override suspend fun setUserLanguage(language: String) {
        userRepository.setUserLanguage(language)
    }
}
