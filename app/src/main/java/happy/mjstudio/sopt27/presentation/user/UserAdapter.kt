package happy.mjstudio.sopt27.presentation.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import happy.mjstudio.sopt27.databinding.ItemUserBinding
import happy.mjstudio.sopt27.domain.entity.ReqresUserEntity
import happy.mjstudio.sopt27.presentation.user.UserAdapter.UserHolder

class UserAdapter : ListAdapter<ReqresUserEntity, UserHolder>(DIFF) {
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)

        return UserHolder(binding)
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) = holder.bind(getItem(position))

    inner class UserHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ReqresUserEntity) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ReqresUserEntity>() {
            override fun areItemsTheSame(oldItem: ReqresUserEntity, newItem: ReqresUserEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ReqresUserEntity, newItem: ReqresUserEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}

@BindingAdapter("app:recyclerview_reqresUserEntity_items")
fun RecyclerView.setItems(items: List<ReqresUserEntity>?) {
    (adapter as? UserAdapter)?.run {
        this.submitList(items ?: listOf())
    }
}