package happy.mjstudio.sopt27.presentation.frame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import dagger.hilt.android.AndroidEntryPoint
import happy.mjstudio.sopt27.R
import happy.mjstudio.sopt27.databinding.FragmentFrameBinding
import happy.mjstudio.sopt27.utils.AutoClearedValue

@AndroidEntryPoint
class FrameFragment : Fragment() {

    private var mBinding: FragmentFrameBinding by AutoClearedValue()
    private val viewModel by viewModels<FrameViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentFrameBinding.inflate(inflater, container, false).let {
            mBinding = it
            it.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.vm = viewModel

        configurePager()
        configureBottomNavigation()
        observeViewModel()
    }

    private fun configurePager() = mBinding.pager.run {
        offscreenPageLimit = 3
        adapter = FrameAdapter(this@FrameFragment)
        registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.onPageSelected(position)
            }
        })
        setPageTransformer { page, position ->
            page.pivotX = if (position < 0) page.width.toFloat() else 0f
            page.pivotY = page.height * 0.5f
            page.rotationY = 50f * position
        }
    }

    private fun configureBottomNavigation() = mBinding.bottomNavigation.run {
        setOnNavigationItemSelectedListener {
            viewModel.onPageSelected(
                when (it.itemId) {
                    R.id.profileFragment -> 0
                    R.id.mainFragment -> 1
                    R.id.settingsFragment -> 2
                    else -> throwUnknownMenuSelectedException()
                }
            )
            true
        }
    }

    private fun observeViewModel() {
        viewModel.pageIdx.observe(viewLifecycleOwner) { pageIdx ->
            mBinding.pager.isUserInputEnabled = pageIdx != 1
            scrollPagerToMenu(pageIdx)
            selectBottomNavigationMenu(pageIdx)
        }
    }

    private fun scrollPagerToMenu(pageIdx: Int) {
        mBinding.pager.currentItem = pageIdx
    }

    private fun selectBottomNavigationMenu(pageIdx: Int) {
        mBinding.bottomNavigation.selectedItemId = when (pageIdx) {
            0 -> R.id.profileFragment
            1 -> R.id.mainFragment
            2 -> R.id.settingsFragment
            else -> throwUnknownMenuSelectedException()
        }
    }

    private fun throwUnknownMenuSelectedException(): Nothing = throw RuntimeException("How dare you...")
}