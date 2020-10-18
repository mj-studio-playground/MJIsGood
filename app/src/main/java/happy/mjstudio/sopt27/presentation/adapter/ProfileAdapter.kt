package happy.mjstudio.sopt27.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import happy.mjstudio.sopt27.databinding.ItemProfileBinding
import happy.mjstudio.sopt27.model.Profile
import happy.mjstudio.sopt27.presentation.adapter.ProfileAdapter.ProfileHolder

class ProfileAdapter : ListAdapter<Profile, ProfileHolder>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Profile>() {
            override fun areItemsTheSame(oldItem: Profile, newItem: Profile): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Profile, newItem: Profile): Boolean {
                return oldItem == newItem
            }
        }
    }

    fun submitItems(items: List<Profile>) {
        submitList(items)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemProfileBinding.inflate(inflater, parent, false)

        return ProfileHolder(binding)
    }


    override fun onBindViewHolder(holder: ProfileHolder, position: Int) = holder.bind(getItem(position))


    inner class ProfileHolder(private val binding: ItemProfileBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Profile) {
            binding.item = item
            binding.executePendingBindings()
        }
    }
}

@BindingAdapter("app:recyclerview_profile_items")
fun RecyclerView.setItems(items: List<Profile>?) {
    (adapter as? ProfileAdapter)?.run {
        this.submitItems(items ?: listOf())
    }
}
