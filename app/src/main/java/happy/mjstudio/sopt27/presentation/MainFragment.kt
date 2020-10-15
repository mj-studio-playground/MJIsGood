package happy.mjstudio.sopt27.presentation

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import happy.mjstudio.sopt27.databinding.FragmentMainBinding
import happy.mjstudio.sopt27.utils.AutoClearedValue
import happy.mjstudio.sopt27.utils.PrefSettingsManager
import happy.mjstudio.sopt27.utils.showToast
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var mBinding: FragmentMainBinding by AutoClearedValue()

    private var checkAutoSignIn = false

    @Inject
    lateinit var settingManager: PrefSettingsManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentMainBinding.inflate(inflater, container, false).also {
            mBinding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mBinding.lifecycleOwner = viewLifecycleOwner

        checkLastSignInInfo()
        observeArgs()
        setOnDetailButtonClickListener()
        setOnSignUpButtonClickListener()
        startLogoPulseAnim()
    }

    private fun checkLastSignInInfo() {
        if (checkAutoSignIn) return

        lifecycleScope.launchWhenCreated {
            val info = settingManager.lastSignInInfo.first()
            if (info.id.isNotBlank() && info.pw.isNotBlank()) {
                showToast("Auto sign-in success 🚀")
                navigateDetail()
            }
            checkAutoSignIn = true
        }
    }

    private fun observeArgs() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("id")
            ?.observe(viewLifecycleOwner) {
                mBinding.id.setText(it)
            }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("pw")
            ?.observe(viewLifecycleOwner) {
                mBinding.pw.setText(it)
            }
    }

    private fun setOnDetailButtonClickListener() = mBinding.button.setOnClickListener { navigateDetail() }

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

    private fun setOnSignUpButtonClickListener() = mBinding.signUp.setOnClickListener { navigateSignUp() }

    private fun navigateSignUp() {
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 300L
        }
        exitTransition = MaterialElevationScale(true).apply {
            duration = 300L
        }

        val extras = FragmentNavigatorExtras(mBinding.signUp to "signup")
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToSignUpFragment(
                mBinding.id.text?.toString() ?: "", mBinding.pw.text?.toString() ?: ""
            ), extras
        )
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