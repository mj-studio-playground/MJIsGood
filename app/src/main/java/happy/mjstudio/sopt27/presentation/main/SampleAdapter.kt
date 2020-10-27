package happy.mjstudio.sopt27.presentation.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import happy.mjstudio.sopt27.databinding.ItemSampleBinding
import happy.mjstudio.sopt27.domain.entity.Sample
import happy.mjstudio.sopt27.presentation.main.SampleAdapter.SampleHolder
import happy.mjstudio.sopt27.utils.ItemTouchHelperAdapter
import happy.mjstudio.sopt27.utils.PixelRatio
import happy.mjstudio.sopt27.utils.SwipeMenuTouchListener
import happy.mjstudio.sopt27.utils.SwipeMenuTouchListener.Callback
import happy.mjstudio.sopt27.utils.onDebounceClick
import java.util.*

class SampleAdapter(private val pixelRatio: PixelRatio) : RecyclerView.Adapter<SampleHolder>(), ItemTouchHelperAdapter {
    private val samples = mutableListOf<Sample>()
    private val menuOpens = mutableListOf<Boolean>()

    fun submitItems(items: List<Sample>) {
        samples.clear()
        menuOpens.clear()
        samples.addAll(items)
        menuOpens.addAll(List(items.size) { false })
        notifyDataSetChanged()
    }

    override fun getItemCount() = samples.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SampleHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSampleBinding.inflate(inflater, parent, false)

        return SampleHolder(binding)
    }

    override fun onBindViewHolder(holder: SampleHolder, position: Int) =
        holder.bind(samples[position], menuOpens[position])

    inner class SampleHolder(private val binding: ItemSampleBinding) : RecyclerView.ViewHolder(binding.root) {
        private val menuWidth = pixelRatio.toPixel(100).toFloat()
        private var contentTranslationX: Float
            get() = binding.content.translationX
            set(value) {
                binding.content.translationX = value
            }

        init {
            setOnSwipeMenuTouchListener()
            setOnRemoveMenuClickListener()
        }

        @SuppressLint("ClickableViewAccessibility")
        private fun setOnSwipeMenuTouchListener() = binding.content.setOnTouchListener(
            SwipeMenuTouchListener(menuWidth, object : Callback {
                override fun onContentXChanged(x: Float) {
                    contentTranslationX = x
                }

                override fun onContentXAnimated(x: Float) {
                    animateTranslationX(x)
                }

                override fun onMenuOpened() {
                    menuOpens[layoutPosition] = true
                }

                override fun onMenuClosed() {
                    menuOpens[layoutPosition] = false
                }
            })
        )

        private fun animateTranslationX(to: Float) {
            binding.content.animate().translationX(to).apply {
                duration = 100L
            }
        }

        private fun setOnRemoveMenuClickListener() = binding.menu onDebounceClick {
            removeItem(layoutPosition)
        }

        fun bind(item: Sample, isMenuOpen: Boolean) {
            contentTranslationX = if (isMenuOpen) -menuWidth else 0f
            binding.content.translationX

            binding.root.transitionName = item.title
            binding.item = item
            binding.executePendingBindings()
        }
    }


    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(samples, i, i + 1)
                Collections.swap(menuOpens, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(samples, i, i - 1)
                Collections.swap(menuOpens, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    fun removeItem(position: Int) {
        menuOpens.removeAt(position)
        samples.removeAt(position)
        notifyItemRemoved(position)
    }
}
