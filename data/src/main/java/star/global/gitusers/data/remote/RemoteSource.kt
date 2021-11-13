package star.global.gitusers.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import star.global.gitusers.data.remote.response.UserDto
import star.global.gitusers.data.remote.response.UserSearchDto

interface RemoteSource {
    @GET("/search/users?type=users")
    suspend fun fetchUsers(
        @Query("q", encoded = true) query: String,
        @Query("page") page: Int
    ): UserSearchDto

    @GET("/users/{username}")
    suspend fun fetchUserDetail(@Path("username") username: String): UserDto
}