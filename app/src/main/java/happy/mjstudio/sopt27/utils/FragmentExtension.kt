package happy.mjstudio.sopt27.utils

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showToast(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}

fun Fragment.onBackPressed() {
    requireActivity().onBackPressedDispatcher.onBackPressed()
}