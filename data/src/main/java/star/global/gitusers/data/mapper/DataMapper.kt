package star.global.gitusers.data.mapper

import retrofit2.HttpException
import star.global.gitusers.data.remote.response.UserDto
import star.global.gitusers.domain.Error
import star.global.gitusers.domain.User
import java.io.IOException
import java.net.UnknownHostException

fun Throwable.toLocalError(): Error = when (this) {
    is IOException, is UnknownHostException -> Error.network()
    is HttpException -> Error.errorData(code(), message(), response())
    else -> Error.unknown()
}

fun UserDto.toUser(): User = User(
    username = login,
    name = name ?: "N/A",
    avatarUrl = avatar_url ?: "",
    company = company ?: "N/A",
    location = location ?: "N/A",
    email = email ?: "N/A",
    bio = bio ?: "N/A",
    followers = followers ?: 0,
    following = following ?: 0,
    repos = public_repos ?: 0,
    gists = public_gists ?: 0,
)