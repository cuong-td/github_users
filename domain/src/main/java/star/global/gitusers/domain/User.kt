package star.global.gitusers.domain

data class User(
    val username: String,
    val name: String,
    val avatarUrl: String,
    val company: String,
    val location: String,
    val email: String,
    val bio: String,
    val followers: Int,
    val following: Int,
    val repos: Int,
    val gists: Int,
)
