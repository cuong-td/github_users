package star.global.gitusers.data.remote.response

data class UserSearchDto(
    val incomplete_results: Boolean?,
    val items: List<BriefUserDto>?,
    val total_count: Int?
)