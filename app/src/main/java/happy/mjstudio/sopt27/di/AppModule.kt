package happy.mjstudio.sopt27.di

import android.app.Application
import android.content.Context
import android.util.DisplayMetrics
import com.thedeanda.lorem.LoremIpsum
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import happy.mjstudio.sopt27.utils.PixelRatio
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideContext(app: Application): Context = app

    @Provides
    @Singleton
    fun provideDisplayMetrics(context: Context): DisplayMetrics = context.resources.displayMetrics

    @Provides
    @Singleton
    fun providePixelRatio(displayMetrics: DisplayMetrics) = PixelRatio(displayMetrics)

    @Provides
    fun provideLoremIpsum() = LoremIpsum.getInstance()
}