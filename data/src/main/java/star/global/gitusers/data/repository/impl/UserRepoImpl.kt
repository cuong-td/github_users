package star.global.gitusers.data.repository.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import star.global.gitusers.data.mapper.toModel
import star.global.gitusers.data.mapper.toUser
import star.global.gitusers.data.remote.RemoteSource
import star.global.gitusers.data.remote.request.toQueryFormat
import star.global.gitusers.data.repository.BaseRepository
import star.global.gitusers.data.repository.UserRepository
import star.global.gitusers.domain.Either
import star.global.gitusers.domain.Error
import star.global.gitusers.domain.right
import star.global.gitusers.domain.user.SearchData
import star.global.gitusers.domain.user.User
import javax.inject.Inject

class UserRepoImpl
@Inject
constructor(private val remoteSource: RemoteSource) : UserRepository, BaseRepository() {
    override suspend fun fetchUsers(
        keyword: String,
        page: Int
    ): Flow<Either<Error, SearchData>> = flow {
        val either = safeExecution {
            val dto = remoteSource.fetchUsers(keyword.toQueryFormat(), page)
            dto.toModel().right()
        }
        emit(either)
    }

    override suspend fun fetchUser(name: String): Flow<Either<Error, User>> = flow {
        val either = safeExecution {
            val dto = remoteSource.fetchUserDetail(name)
            dto.toUser().right()
        }
        emit(either)
    }
}