package happy.mjstudio.sopt27.presentation.frame

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import happy.mjstudio.sopt27.presentation.MainFragmentFactory
import happy.mjstudio.sopt27.presentation.main.MainFragment
import happy.mjstudio.sopt27.presentation.profile.ProfileFragment
import happy.mjstudio.sopt27.presentation.settings.SettingsFragment

class FrameAdapter(private val fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        val classLoader = fragment.requireActivity().classLoader
        val factory = MainFragmentFactory.getInstance(fragment.requireActivity())
        return when (position) {
            0 -> factory.instantiate(classLoader, ProfileFragment::class.java.name)
            1 -> factory.instantiate(classLoader, MainFragment::class.java.name)
            2 -> factory.instantiate(classLoader, SettingsFragment::class.java.name)
            else -> throw RuntimeException("What the...")
        }
    }
}