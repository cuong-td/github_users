package star.global.gitusers.usecase.impl

import star.global.gitusers.data.repository.UserRepository
import star.global.gitusers.usecase.GetUserDetail
import javax.inject.Inject

class GetUserImpl
@Inject
constructor(private val repo: UserRepository) : GetUserDetail {
    override suspend fun invoke(username: String) = repo.fetchUser(username)
}