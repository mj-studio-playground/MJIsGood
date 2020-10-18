package happy.mjstudio.sopt27.presentation.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import happy.mjstudio.sopt27.databinding.FragmentSignUpBinding
import happy.mjstudio.sopt27.presentation.SignUpFragmentArgs
import happy.mjstudio.sopt27.utils.AutoClearedValue
import happy.mjstudio.sopt27.utils.PrefSettingsManager
import happy.mjstudio.sopt27.utils.showToast
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var mBinding: FragmentSignUpBinding by AutoClearedValue()

    @Inject
    lateinit var settingManager: PrefSettingsManager

    private val args by navArgs<SignUpFragmentArgs>()

    private val name = MutableLiveData("")
    private val id = MutableLiveData("")
    private val pw = MutableLiveData("")

    private val isFormsValid: Boolean
        get() = !id.value.isNullOrBlank() && !name.value.isNullOrBlank() && !pw.value.isNullOrBlank()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentSignUpBinding.inflate(inflater, container, false).let {
            mBinding = it
            it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        id.value = args.id
        pw.value = args.pw
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.nameValue = name
        mBinding.idValue = id
        mBinding.pwValue = pw
        setTransition()

        setOnSignUpButtonListener()
    }

    private fun setTransition() {
        enterTransition = MaterialElevationScale(true).apply {
            duration = 300L
        }
        returnTransition = MaterialElevationScale(true).apply {
            duration = 300L
        }
    }

    private fun setOnSignUpButtonListener() = mBinding.signUp.setOnClickListener {
        if (isFormsValid) {
            findNavController().previousBackStackEntry!!.savedStateHandle["id"] = id.value
            findNavController().previousBackStackEntry!!.savedStateHandle["pw"] = pw.value

            showToast("SignUp Success ✅")

            lifecycleScope.launch {
                settingManager.updateLastSignInInfo(id.value!!, pw.value!!)
                findNavController().popBackStack()
            }
        } else {
            showToast("Fill the all forms 💥")
        }
    }

}