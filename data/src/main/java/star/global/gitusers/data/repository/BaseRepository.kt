package star.global.gitusers.data.repository

import star.global.gitusers.data.mapper.toLocalError
import star.global.gitusers.domain.Either
import star.global.gitusers.domain.Error
import star.global.gitusers.domain.left

abstract class BaseRepository {
    protected inline fun <T> safeExecution(execute: () -> Either.Right<Nothing, T>): Either<Error, T> {
        runCatching {
            return execute()
        }.onFailure {
            return it.toLocalError().left()
        }
        return Error.unknown().left()
    }
}