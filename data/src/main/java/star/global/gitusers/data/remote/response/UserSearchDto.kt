package star.global.gitusers.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserSearchDto(
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean?,
    val items: List<BriefUserDto>?,
    @SerializedName("total_count")
    val totalCount: Int?
)