package star.global.gitusers.presentation.user

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import star.global.gitusers.domain.user.BriefUser
import star.global.gitusers.domain.user.User

class UserDetailBindingModel {
    val username = ObservableField("N/A")
    val avatar = ObservableField("")
    val name = ObservableField("N/A")
    val company = ObservableField("N/A")
    val location = ObservableField("N/A")
    val email = ObservableField("N/A")
    val bio = ObservableField("N/A")
    val followers = ObservableInt(0)
    val following = ObservableInt(0)
    val repos = ObservableField("0")
    val gists = ObservableField("0")

    fun bind(data: User) {
        name.set(data.name)
        company.set(data.company)
        location.set(data.location)
        email.set(data.email)
        bio.set(data.bio)
        followers.set(data.followers)
        following.set(data.following)
        repos.set(data.repos.toString())
        gists.set(data.gists.toString())
    }

    fun bind(brief: BriefUser) {
        username.set(brief.username)
        avatar.set(brief.username)
    }
}