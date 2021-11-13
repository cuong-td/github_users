package star.global.gitusers.data.repository

import kotlinx.coroutines.flow.Flow
import star.global.gitusers.domain.Either
import star.global.gitusers.domain.Error
import star.global.gitusers.domain.user.BriefUser
import star.global.gitusers.domain.user.User

interface UserRepository {
    suspend fun fetchUsers(
        keyword: String,
        page: Int = 1
    ): Flow<Either<Error, List<BriefUser>>>

    suspend fun fetchUser(
        name: String,
    ): Flow<Either<Error, User>>

}