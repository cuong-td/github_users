package star.global.gitusers.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import star.global.gitusers.databinding.ItemSearchBinding
import star.global.gitusers.domain.user.BriefUser
import star.global.gitusers.extension.setOnDebounceClick

class UserSearchAdapter(private val itemClick: (String) -> Unit) :
    ListAdapter<BriefUser, Holder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val viewBinding = ItemSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).apply {
            bindingModel = ItemSearchBindingModel()
        }
        return Holder(viewBinding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val itemData = getItem(position)
        holder.bindData(itemData)
        holder.itemView.setOnDebounceClick {
            itemClick(itemData.username)
        }
    }
}

class Holder(private val viewBinding: ItemSearchBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {

    fun bindData(data: BriefUser) {
        viewBinding.bindingModel?.bind(data)
    }
}

private class DiffCallback : DiffUtil.ItemCallback<BriefUser>() {
    override fun areItemsTheSame(oldItem: BriefUser, newItem: BriefUser): Boolean {
        return oldItem.username == newItem.username
    }

    override fun areContentsTheSame(oldItem: BriefUser, newItem: BriefUser): Boolean {
        return areItemsTheSame(oldItem, newItem) && oldItem.avatarUrl == newItem.avatarUrl
    }
}
