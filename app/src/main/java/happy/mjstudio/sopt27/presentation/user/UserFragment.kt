package happy.mjstudio.sopt27.presentation.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import happy.mjstudio.sopt27.databinding.FragmentUserBinding
import happy.mjstudio.sopt27.utils.AutoClearedValue

@AndroidEntryPoint
class UserFragment : Fragment() {

    private var mBinding: FragmentUserBinding by AutoClearedValue()
    private val viewModel by viewModels<UserViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentUserBinding.inflate(inflater, container, false).let {
            mBinding = it
            it.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.vm = viewModel

        configureList()
    }

    private fun configureList() = mBinding.list.run {
        adapter = UserAdapter()
    }

}