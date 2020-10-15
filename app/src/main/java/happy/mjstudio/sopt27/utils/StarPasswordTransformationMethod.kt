package happy.mjstudio.sopt27.utils

import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter

class StarPasswordTransformationMethod : PasswordTransformationMethod() {
    override fun getTransformation(source: CharSequence, view: View): CharSequence {
        return PasswordCharSequence(source)
    }


    private inner class PasswordCharSequence(private val mSource: CharSequence) // Store char sequence
        : CharSequence {

        override fun get(index: Int): Char {
            return '✿'
        }


        override val length: Int
            get() = mSource.length

        override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
            return mSource.subSequence(startIndex, endIndex) // Return default
        }
    }
}

@BindingAdapter("app:starTransformation", requireAll = false)
fun EditText.setStarTransformMethod(enable: Boolean) {
    transformationMethod = StarPasswordTransformationMethod()
}
