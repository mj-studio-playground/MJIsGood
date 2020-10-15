package happy.mjstudio.sopt27.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialContainerTransform
import happy.mjstudio.sopt27.R
import happy.mjstudio.sopt27.databinding.FragmentSignUpBinding
import happy.mjstudio.sopt27.utils.AutoClearedValue
import happy.mjstudio.sopt27.utils.showToast

class SignUpFragment : Fragment() {

    private var mBinding: FragmentSignUpBinding by AutoClearedValue()

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
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = 300L
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().getColor(R.color.colorBackground))
        }
    }

    private fun setOnSignUpButtonListener() = mBinding.signUp.setOnClickListener {
        if (isFormsValid) {
            findNavController().previousBackStackEntry!!.savedStateHandle["id"] = id.value
            findNavController().previousBackStackEntry!!.savedStateHandle["pw"] = pw.value
            showToast("SignUp Success ✅")
            findNavController().popBackStack()
        } else {
            showToast("Fill the all forms 💥")
        }
    }

}