package happy.mjstudio.sopt27.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import happy.mjstudio.sopt27.databinding.FragmentSearchBinding
import happy.mjstudio.sopt27.utils.AutoClearedValue
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.widget.textChanges

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var mBinding: FragmentSearchBinding by AutoClearedValue()
    private val viewModel by viewModels<SearchViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentSearchBinding.inflate(inflater, container, false).let {
            mBinding = it
            it.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.vm = viewModel

        configureList()
        configureEditText()
    }

    private fun configureList() = mBinding.list.run {
        adapter = SearchAdapter()
    }

    private fun configureEditText() = mBinding.editText.run {
        textChanges().debounce(1500L).onEach {
            if (it.isNotEmpty()) viewModel.search(it.toString())
        }.launchIn(lifecycleScope)
    }
}