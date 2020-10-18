package happy.mjstudio.sopt27.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import happy.mjstudio.sopt27.databinding.ItemSampleBinding
import happy.mjstudio.sopt27.model.Sample
import happy.mjstudio.sopt27.presentation.main.SampleAdapter.SampleHolder

class SampleAdapter : ListAdapter<Sample, SampleHolder>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Sample>() {
            override fun areItemsTheSame(oldItem: Sample, newItem: Sample): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Sample, newItem: Sample): Boolean {
                return oldItem == newItem
            }
        }
    }

    fun submitItems(items: List<Sample>) {
        submitList(items)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SampleHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSampleBinding.inflate(inflater, parent, false)

        return SampleHolder(binding)
    }


    override fun onBindViewHolder(holder: SampleHolder, position: Int) = holder.bind(getItem(position))


    inner class SampleHolder(private val binding: ItemSampleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Sample) {
            binding.item = item
            binding.executePendingBindings()
        }
    }
}

@BindingAdapter("app:recyclerview_profile_items")
fun RecyclerView.setItems(items: List<Sample>?) {
    (adapter as? SampleAdapter)?.run {
        this.submitItems(items ?: listOf())
    }
}
