package happy.mjstudio.sopt27.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import happy.mjstudio.sopt27.utils.NativeLib
import happy.mjstudio.sopt27.utils.NativeLibImpl
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
abstract class NativeLibModule {
    @Binds
    @Singleton
    abstract fun bindNativeLib(nativeLib: NativeLibImpl): NativeLib
}