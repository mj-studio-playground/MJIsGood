package happy.mjstudio.sopt27.presentation.signin

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import happy.mjstudio.sopt27.R
import happy.mjstudio.sopt27.databinding.FragmentSignInBinding
import happy.mjstudio.sopt27.utils.AutoClearedValue
import happy.mjstudio.sopt27.utils.BioAuth
import happy.mjstudio.sopt27.utils.observeEvent
import happy.mjstudio.sopt27.utils.onDebounceClick
import happy.mjstudio.sopt27.utils.showToast

@AndroidEntryPoint
class SignInFragment(private val bioAuth: BioAuth) : Fragment() {
    private var mBinding: FragmentSignInBinding by AutoClearedValue()
    private val viewModel by viewModels<SignInViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentSignInBinding.inflate(inflater, container, false).also {
            mBinding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = viewModel

        observeArgs()
        tryAutoSignIn()
        observeSignInResult()
        setOnThemeButtonClickListener()
        setOnSignInButtonClickListener()
        setOnBioSignInButtonClickListener()
        setOnSignUpButtonListener()
        startLogoPulseAnim()
    }

    private fun observeArgs() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("id")
            ?.observe(viewLifecycleOwner) {
                viewModel.id.value = it
            }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("pw")
            ?.observe(viewLifecycleOwner) {
                viewModel.pw.value = it
            }
    }

    private fun tryAutoSignIn() = lifecycleScope.launchWhenStarted {
        if (viewModel.canAutoSignIn()) {
            showToast("Auto sign-in success 🚀")
            navigateMain()
        }
    }

    private fun setOnSignInButtonClickListener() = mBinding.button onDebounceClick {
        viewModel.tryManualSignIn()
    }

    private fun setOnBioSignInButtonClickListener() = mBinding.bioButton onDebounceClick {
        if (!bioAuth.biometricEnabled) {
            showToast("This device doesn't support biometric authentication")
        } else {
            tryBiometricAuth()
        }
    }

    private fun tryBiometricAuth() = lifecycleScope.launchWhenStarted {
        if (bioAuth.authenticate(this@SignInFragment)) {
            showToast("Success 🚀")
        } else {
            showToast("Fail 💥")
        }
    }

    private fun observeSignInResult() {
        observeEvent(viewModel.onSignInSuccess) {
            showToast("SignIn Success ⭐️")
            navigateMain()
        }
        observeEvent(viewModel.onSignInFail) {
            showToast("SignIn fail 💥")
        }
    }

    private fun navigateMain() {
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 300L
        }
        exitTransition = MaterialElevationScale(true).apply {
            duration = 300L
        }

        /**
         * why this cause fucking memory leak -.-
         */
        //        val extras = FragmentNavigatorExtras(mBinding.title to "title")
        findNavController().navigate(R.id.action_signInFragment_to_mainFragment, null, null)
    }

    private fun setOnSignUpButtonListener() = mBinding.signUp.onDebounceClick { navigateSignUp() }

    private fun navigateSignUp() {
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 300L
        }
        exitTransition = MaterialElevationScale(true).apply {
            duration = 300L
        }

        findNavController().navigate(
            SignInFragmentDirections.actionSignInFragmentToSignUpFragment(
                viewModel.id.value!!, viewModel.pw.value!!
            )
        )
    }

    private fun setOnThemeButtonClickListener() = mBinding.switchTheme onDebounceClick {
        val nightMode = AppCompatDelegate.getDefaultNightMode()
        AppCompatDelegate.setDefaultNightMode(if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES)
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