package star.global.gitusers.data.mapper

import star.global.gitusers.domain.Error
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

fun Throwable.toLocalError(): Error = when (this) {
    is IOException, is UnknownHostException -> Error.network()
    is HttpException -> Error.errorData(code(), message(), response())
    else -> Error.unknown()
}