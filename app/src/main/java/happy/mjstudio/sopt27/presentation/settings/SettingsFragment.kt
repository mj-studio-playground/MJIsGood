package happy.mjstudio.sopt27.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import happy.mjstudio.sopt27.databinding.FragmentSettingsBinding
import happy.mjstudio.sopt27.utils.AutoClearedValue

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var mBinding: FragmentSettingsBinding by AutoClearedValue()
    private val viewModel by viewModels<SettingsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentSettingsBinding.inflate(inflater, container, false).let {
            mBinding = it
            it.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.vm = viewModel
    }

}