package happy.mjstudio.sopt27.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.Tab
import dagger.hilt.android.AndroidEntryPoint
import happy.mjstudio.sopt27.databinding.FragmentProfileBinding
import happy.mjstudio.sopt27.utils.AutoClearedValue

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var mBinding: FragmentProfileBinding by AutoClearedValue()
    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentProfileBinding.inflate(inflater, container, false).let {
            mBinding = it
            it.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.vm = viewModel

        configurePager()
        configureTabLayout()
        observeViewModel()
    }

    private fun configurePager() = mBinding.pager.run {
        isUserInputEnabled = false
    }

    private fun configureTabLayout() = mBinding.tabLayout.run {
        this.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: Tab?) {
                tab?.position?.run {
                    viewModel.onTabIdxChanged(this)
                }
            }

            override fun onTabUnselected(tab: Tab?) {
            }

            override fun onTabReselected(tab: Tab?) {
            }
        })
    }

    private fun observeViewModel() = viewModel.run {
        tabIdx.observe(viewLifecycleOwner) {

        }
    }

}