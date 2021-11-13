package star.global.gitusers.usecase

import kotlinx.coroutines.flow.Flow
import star.global.gitusers.domain.Either
import star.global.gitusers.domain.Error
import star.global.gitusers.domain.user.User

interface GetUserDetail {
    suspend operator fun invoke(username: String): Flow<Either<Error, User>>
}