package star.global.gitusers.usecase.impl

import star.global.gitusers.data.repository.UserRepository
import star.global.gitusers.usecase.FindUsers
import javax.inject.Inject

class FindUsersImpl
@Inject
constructor(private val repo: UserRepository) : FindUsers {
    override suspend fun invoke(keywords: String, page: Int) = repo.fetchUsers(keywords, page)
}