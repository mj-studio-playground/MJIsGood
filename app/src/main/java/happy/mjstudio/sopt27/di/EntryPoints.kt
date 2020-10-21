package happy.mjstudio.sopt27.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import happy.mjstudio.sopt27.utils.PixelRatio
import javax.inject.Singleton

@EntryPoint
@InstallIn(ApplicationComponent::class)
interface ApplicationEntryPoint {
    @Singleton
    fun pixelRatio(): PixelRatio
}