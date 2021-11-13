package star.global.gitusers.usecase

import kotlinx.coroutines.flow.Flow
import star.global.gitusers.domain.Either
import star.global.gitusers.domain.Error
import star.global.gitusers.domain.user.BriefUser

interface FindUsers {
    suspend operator fun invoke(keywords: String, page: Int): Flow<Either<Error, List<BriefUser>>>
}