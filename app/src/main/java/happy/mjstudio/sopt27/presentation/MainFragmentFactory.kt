package happy.mjstudio.sopt27.presentation

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent
import happy.mjstudio.sopt27.presentation.main.MainFragment
import happy.mjstudio.sopt27.presentation.signin.SignInFragment
import happy.mjstudio.sopt27.presentation.signup.SignUpFragment
import happy.mjstudio.sopt27.utils.PixelRatio
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
class MainFragmentFactory(activity: Activity) : FragmentFactory() {

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface MainFragmentFactoryEntryPoint {
        fun pixelRatio(): PixelRatio
    }

    private val entryPoint = EntryPointAccessors.fromActivity(activity, MainFragmentFactoryEntryPoint::class.java)

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (loadFragmentClass(classLoader, className)) {
            MainFragment::class.java -> MainFragment(entryPoint.pixelRatio())
            SignInFragment::class.java -> SignInFragment()
            SignUpFragment::class.java -> SignUpFragment()
            else -> super.instantiate(classLoader, className)
        }
    }

    companion object {
        fun getInstance(activity: Activity): MainFragmentFactory {
            return MainFragmentFactory(activity)
        }
    }
}