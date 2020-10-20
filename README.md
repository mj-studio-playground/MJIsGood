# MJIsGood
<!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
[![All Contributors](https://img.shields.io/badge/all_contributors-1-orange.svg?style=flat-square)](#contributors-)
<!-- ALL-CONTRIBUTORS-BADGE:END -->

## 24124124th Android sample

<img src="1.gif" />

## Contents

- [Feature](#feature)
  
  [week #1](#assignment-1)
  
  [week #2](#assignment-2)
- [What to learn](#what-to-learn)
  
  [week #1](#assignment-3)
  
  [week #2](#assignment-4)
- [Checkout date](#checkout-date)
- [Contributors](#contributors-)

## Feature

#### Assignment #1
- Sign-up
- Sign-in
- Auto sign-in
- Form validation
- Switch dark theme

#### Assignment #2
- Show items in list
- Drag items in list
- Swipe items in list
- Delete items in list

## What to learn

#### Assignment #1
- AAC Lifecycle(LiveData, ViewModel)

*SignInViewModel.kt*
```kotlin
typealias AutoSignIn = Boolean

class SignInViewModel @ViewModelInject constructor(
    private val settingsManager: PrefSettingsManager, /*@Assisted private val savedStateHandle: SavedStateHandle*/
) : ViewModel() {
    // StateFlow data binding support is coming in AGP 4.3
    // https://twitter.com/manuelvicnt/status/1314621067831521282
    val id = MutableLiveData("")
    val pw = MutableLiveData("")

    private var isAutoSignInTried = false

    val onSignInSuccess = EventLiveData<AutoSignIn>()
    val onSignInFail = EventLiveData<AutoSignIn>()

    private suspend fun matchWithLastSignInInfo(): Boolean {
        val (lastId, lastPw) = settingsManager.lastSignInInfo.first()
        return id.value == lastId && pw.value == lastPw && lastId.isNotBlank() && lastPw.isNotBlank()
    }

    suspend fun canAutoSignIn(): Boolean {
        if (isAutoSignInTried) return false
        isAutoSignInTried = true

        val (lastId, lastPw) = settingsManager.lastSignInInfo.first()
        val result = lastId.isNotBlank() && lastPw.isNotBlank()
        if (result) {
            id.value = lastId
            pw.value = lastPw
        }
        return result
    }

    fun tryManualSignIn() = viewModelScope.launch {
        if (matchWithLastSignInInfo()) {
            onSignInSuccess.emit(false)
        } else {
            onSignInFail.emit(false)
        }
    }
}
```

- AAC DataBinding, ViewBinding
- Kotlin Coroutine(Flow, StateFlow, SharedFlow, ...)

```kotlin
lifecycleScope.launch {
    settingManager.updateLastSignInInfo(id.value!!, pw.value!!)
    findNavController().popBackStack()
}
```

- AAC Navigation

```kotlin
findNavController().navigate(
    SignInFragmentDirections.actionSignInFragmentToSignUpFragment(
        viewModel.id.value!!, viewModel.pw.value!!
    )
)
```

- Hilt

*AppModule.kt*
```kotlin
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
}
```

- Kotlin gradle script
- Kotlin stdlib
- ConstraintLayout
- MDC
- DataStore

*PrefSettingsManager.kt*
```kotlin
@Singleton
class PrefSettingsManager @Inject constructor(context: Context) {
    private val dataStore = context.createDataStore("setting")

    val lastSignInInfo: Flow<LastSignInInfo> = dataStore.data.catch { e ->
        if (e is IOException) {
            emit(emptyPreferences())
        } else {
            throw e
        }
    }.map { pref ->
        val id = pref[ID_KEY] ?: ""
        val pw = pref[PW_KEY] ?: ""

        LastSignInInfo(id, pw)
    }

    suspend fun updateLastSignInInfo(id: String, pw: String) {
        dataStore.edit { pref ->
            pref[ID_KEY] = id
            pref[PW_KEY] = pw
        }
    }

    companion object {
        private val ID_KEY = preferencesKey<String>("id")
        private val PW_KEY = preferencesKey<String>("pw")
    }
}
```

#### Assignment #2
- RecyclerView
- ItemTouchHelper
- SwipeMenuTouchListener

*SwipeMenuTouchListener.kt*
```kotlin
class SwipeMenuTouchListener(
    private val menuWidth: Float, private val callback: Callback
) : OnTouchListener {
    private var dx = 0f

    override fun onTouch(view: View, e: MotionEvent): Boolean {
        when (e.actionMasked) {
            MotionEvent.ACTION_MOVE -> {
                callback.onContentXChanged(e.rawX + dx)
            }
            MotionEvent.ACTION_DOWN -> {
                dx = view.x - e.rawX
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                if (e.rawX + dx < -menuWidth) {
                    callback.onContentXAnimated(-menuWidth)
                    callback.onMenuOpened()
                } else {
                    callback.onContentXAnimated(0f)
                    callback.onMenuClosed()
                }
            }
        }

        return false
    }

    interface Callback {
        fun onContentXChanged(x: Float)
        fun onContentXAnimated(x: Float)
        fun onMenuOpened()
        fun onMenuClosed()
    }
}
```

- OnDebounceClickListener

*OnDebounceClickListener.kt*
```kotlin
class OnDebounceClickListener(private val listener: OnClickListener) : View.OnClickListener {
    override fun onClick(v: View?) {
        val now = System.currentTimeMillis()
        if (now < lastTime + INTERVAL) return
        lastTime = now
        v?.run(listener)
    }

    companion object {
        private const val INTERVAL: Long = 300L
        private var lastTime: Long = 0
    }
}


infix fun View.onDebounceClick(listener: OnClickListener) {
    this.setOnClickListener(OnDebounceClickListener {
        it.run(listener)
    })
}
```

- EventLiveData

*EventLiveData.kt*
```kotlin
class EventLiveData<T> : MutableLiveData<T>() {
    private val pending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner) {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        }
    }

    @MainThread
    fun emit(value: T) {
        pending.set(true)
        setValue(value)
    }
}
```

- PixelRatio

*PixelRatio.kt*
```kotlin
@Singleton
class PixelRatio @Inject constructor(private val displayMetrics: DisplayMetrics) {
    val screenWidth: Int
        get() = displayMetrics.widthPixels

    val screenHeight: Int
        get() = displayMetrics.heightPixels

    @Px
    fun toPixel(dp: Int) = (dp * displayMetrics.density).roundToInt()

    fun toDP(@Px pixel: Int) = (pixel / displayMetrics.density).roundToInt()
}
```

We can test `PixelRatio` with mocking Android instance(`DisplayMetrics`) with **Mockito**.
```kotlin
class PixelRatioTest {
    private lateinit var pixelRatio: PixelRatio

    private val mockWidth = 1000
    private val mockHeight = 2000

    @Before
    fun setup() {
        val mockDisplayMetrics = mock(DisplayMetrics::class.java).apply {
            widthPixels = mockWidth
            heightPixels = mockHeight
            density = 3f
        }

        pixelRatio = PixelRatio(mockDisplayMetrics)
    }

    @Test
    fun `screenWidth, screenHeight should be same with real screen size`() {
        Truth.assertThat(pixelRatio.screenWidth).isEqualTo(mockWidth)
        Truth.assertThat(pixelRatio.screenHeight).isEqualTo(mockHeight)
    }

    @Test
    fun `toDP, toPixel`() {
        Truth.assertThat(pixelRatio.toDP(3)).isEqualTo(1)
        Truth.assertThat(pixelRatio.toPixel(1)).isEqualTo(3)
    }
}
```

- ViewModel
- Suspend function
- OnBackPressedCallback

*MainFragment.kt*
```kotlin
private val backPressedCallback = object : OnBackPressedCallback(true) {
    override fun handleOnBackPressed() {
        isEnabled = false
        if (isCardShowing) {
            hideCard()
        } else {
            onBackPressed()
        }
    }
}

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    requireActivity().onBackPressedDispatcher.addCallback(backPressedCallback)

    ...
}

override fun onDestroyView() {
    super.onDestroyView()
    backPressedCallback.remove()
}
```

- MaterialContainerTransform
- LeakCanary and Android Studio memory profiler

## Checkout date

- assignment #1 2020.10.15
- assignment #2 2020.10.19

## Contributors ✨

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tr>
    <td align="center"><a href="https://www.mjstudio.net/"><img src="https://avatars0.githubusercontent.com/u/33388801?v=4" width="100px;" alt=""/><br /><sub><b>MJ Studio</b></sub></a><br /><a href="https://github.com/mym0404/MJIsGood/commits?author=mym0404" title="Code">💻</a></td>
  </tr>
</table>

<!-- markdownlint-enable -->
<!-- prettier-ignore-end -->
<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!
