package happy.mjstudio.sopt27.presentation

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import happy.mjstudio.sopt27.R
import happy.mjstudio.sopt27.databinding.FragmentMainBinding
import happy.mjstudio.sopt27.utils.AutoClearedValue

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var mBinding: FragmentMainBinding by AutoClearedValue()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentMainBinding.inflate(inflater, container, false).also {
            mBinding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mBinding.lifecycleOwner = viewLifecycleOwner

        setOnButtonClickListener()
        startLogoPulseAnim()
    }

    private fun setOnButtonClickListener() = mBinding.button.setOnClickListener { navigateDetail() }

    private fun showToast() =
        Toast.makeText(requireContext(), "반갑습니다. ${mBinding.id.text}님", Toast.LENGTH_SHORT).apply {
            view.setBackgroundColor(requireContext().getColor(R.color.colorPrimary))
        }.show()

    private fun navigateDetail() {
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 300L
        }
        exitTransition = MaterialElevationScale(true).apply {
            duration = 300L
        }

        val extras = FragmentNavigatorExtras(mBinding.title to "title")
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToDetailFragment(), extras)
    }

    private fun startLogoPulseAnim() {
        startPulseAnim(mBinding.title, "scaleX")
        startPulseAnim(mBinding.title, "scaleY")
    }

    private fun startPulseAnim(target: View, propertyName: String) =
        ObjectAnimator.ofFloat(target, propertyName, 0.95f, 1.05f).apply {
            duration = 500L
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            setAutoCancel(true)
            start()
        }

}