package happy.mjstudio.sopt27.presentation

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.thedeanda.lorem.LoremIpsum
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ActivityComponent
import happy.mjstudio.sopt27.authentication.Authenticator
import happy.mjstudio.sopt27.di.AuthenticatorModule.Companion.AUTHENTICATOR_TYPE
import happy.mjstudio.sopt27.presentation.frame.FrameFragment
import happy.mjstudio.sopt27.presentation.main.MainFragment
import happy.mjstudio.sopt27.presentation.profile.ProfileFragment
import happy.mjstudio.sopt27.presentation.search.SearchFragment
import happy.mjstudio.sopt27.presentation.signin.SignInFragment
import happy.mjstudio.sopt27.presentation.signup.SignUpFragment
import happy.mjstudio.sopt27.presentation.user.UserFragment
import happy.mjstudio.sopt27.utils.BioAuth
import happy.mjstudio.sopt27.utils.PixelRatio
import javax.inject.Named

class MainFragmentFactory(activity: Activity) : FragmentFactory() {

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface MainFragmentFactoryEntryPoint {
        fun pixelRatio(): PixelRatio
        fun loremIpsum(): LoremIpsum

        @Named(AUTHENTICATOR_TYPE)
        fun authenticator(): Authenticator

        fun bioAuth(): BioAuth
    }

    private val entryPoint = EntryPointAccessors.fromActivity(activity, MainFragmentFactoryEntryPoint::class.java)

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (loadFragmentClass(classLoader, className)) {
            FrameFragment::class.java -> FrameFragment()
            MainFragment::class.java -> MainFragment(
                entryPoint.pixelRatio(), entryPoint.loremIpsum(), entryPoint.authenticator()
            )
            SignInFragment::class.java -> SignInFragment(entryPoint.bioAuth())
            SignUpFragment::class.java -> SignUpFragment(entryPoint.authenticator())
            ProfileFragment::class.java -> ProfileFragment()
            UserFragment::class.java -> UserFragment()
            SearchFragment::class.java -> SearchFragment()
            
            else -> super.instantiate(classLoader, className)
        }
    }

    companion object {
        fun getInstance(activity: Activity): MainFragmentFactory {
            return MainFragmentFactory(activity)
        }
    }
}