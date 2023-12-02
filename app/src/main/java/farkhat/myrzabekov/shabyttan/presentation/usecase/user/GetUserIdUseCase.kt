package farkhat.myrzabekov.shabyttan.presentation.usecase.user


interface GetUserIdUseCase {
    suspend operator fun invoke(): Long
}

