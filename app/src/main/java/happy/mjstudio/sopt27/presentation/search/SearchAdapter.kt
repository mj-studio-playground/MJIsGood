package happy.mjstudio.sopt27.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import happy.mjstudio.sopt27.databinding.ItemSearchBinding
import happy.mjstudio.sopt27.domain.entity.KakaoSearchEntity
import happy.mjstudio.sopt27.presentation.search.SearchAdapter.SearchHolder

class SearchAdapter : ListAdapter<KakaoSearchEntity, SearchHolder>(DIFF) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSearchBinding.inflate(inflater, parent, false)

        return SearchHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) = holder.bind(getItem(position))

    inner class SearchHolder(private val binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: KakaoSearchEntity) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<KakaoSearchEntity>() {
            override fun areItemsTheSame(oldItem: KakaoSearchEntity, newItem: KakaoSearchEntity): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: KakaoSearchEntity, newItem: KakaoSearchEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}

@BindingAdapter("app:recyclerview_kakaoSearchEntity_items")
fun RecyclerView.setItems(items: List<KakaoSearchEntity>?) {
    (adapter as? SearchAdapter)?.run {
        this.submitList(items ?: listOf())
    }
}