package star.global.gitusers.presentation.search

import androidx.databinding.ObservableField
import star.global.gitusers.domain.user.BriefUser

class ItemSearchBindingModel {
    val username = ObservableField("")
    val avatar = ObservableField("")

    fun bind(data: BriefUser) {
        username.set(data.username)
        avatar.set(data.avatarUrl)
    }
}