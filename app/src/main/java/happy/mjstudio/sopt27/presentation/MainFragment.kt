package happy.mjstudio.sopt27.presentation

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import happy.mjstudio.sopt27.databinding.FragmentMainBinding
import happy.mjstudio.sopt27.utils.AutoClearedValue
import happy.mjstudio.sopt27.utils.LastSignInInfo
import happy.mjstudio.sopt27.utils.PrefSettingsManager
import happy.mjstudio.sopt27.utils.showToast
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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

        handleSavedInstanceState(savedInstanceState)
        checkLastSignInInfo()
        observeArgs()
        setOnThemeButtonClickListener()
        setOnSignInButtonClickListener()
        setOnSignUpButtonClickListener()
        startLogoPulseAnim()
    }

    private fun handleSavedInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState?.run {
            checkAutoSignIn = getBoolean("checkAutoSignIn")
        }
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

    private fun setOnThemeButtonClickListener() = mBinding.switchTheme.setOnClickListener {
        val nightMode = AppCompatDelegate.getDefaultNightMode()
        AppCompatDelegate.setDefaultNightMode(if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES)
    }

    private fun setOnSignInButtonClickListener() = mBinding.button.setOnClickListener {
        lifecycleScope.launch {
            val lastSignInInfo = getLastSignInInfo()

            val idText = mBinding.id.text?.toString() ?: ""
            val pwText = mBinding.pw.text?.toString() ?: ""

            if (idText == lastSignInInfo.id && pwText == lastSignInInfo.pw && idText.isNotBlank() && pwText.isNotBlank()) {
                navigateDetail()
                showToast("SignIn Success ⭐️")
            } else {
                showToast("SignIn fail 💥")
            }
        }
    }

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

        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToSignUpFragment(
                mBinding.id.text?.toString() ?: "", mBinding.pw.text?.toString() ?: ""
            )
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

    private suspend fun getLastSignInInfo(): LastSignInInfo = settingManager.lastSignInInfo.first()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("checkAutoSignIn", true)
    }
}