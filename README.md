# MJIsGood
<!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
[![All Contributors](https://img.shields.io/badge/all_contributors-1-orange.svg?style=flat-square)](#contributors-)
![Android Build](https://github.com/mym0404/MJIsGood/workflows/Android%20Build/badge.svg)
<!-- ALL-CONTRIBUTORS-BADGE:END -->

## Preview

<img src="1.gif" /> <img src="2.gif" />
## Contents

- [Feature](#feature)
  * [week #1](#assignment-1) 
  * [week #2](#assignment-2)
- [What to learn](#what-to-learn)
  * [week #1](#assignment-1-1)
  * [week #2](#assignment-2-1)
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

- Github action (CI android debug build)
```yml
name: Android Build
on: [push]
defaults:
  run:
    shell: bash
    working-directory: .

jobs:
  build:
    runs-on: ubuntu-latest
    name: build debug
    if: "!contains(toJSON(github.event.commits.*.message), '[skip action]') && !startsWith(github.ref, 'refs/tags/')"
    steps:
      - name: Checkout repository
        uses: actions/checkout@v2
      - name: Gradle cache
        uses: actions/cache@v2
        with:
          path: |
            ~/.gradle/caches
            ~/.gradle/wrapper
          key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*') }}
          restore-keys: |
            ${{ runner.os }}-gradle-
      - name: Build debug
        run: ./gradlew assembleDebug
      - name: Archive artifacts
        uses: actions/upload-artifact@v2
        with:
          path: app/build/outputs
```

#### Assignment #1
- AAC Lifecycle(LiveData, ViewModel)

*SignInViewModel.kt*
```kotlin
typealias AutoSignIn = Boolean

class SignInViewModel @ViewModelInject constructor(
    @Named(AUTHENTICATOR_TYPE) private val authenticator: Authenticator, /*@Assisted private val savedStateHandle: SavedStateHandle*/
) : ViewModel() {
    // StateFlow data binding support is coming in AGP 4.3
    // https://twitter.com/manuelvicnt/status/1314621067831521282
    val id = MutableLiveData("")
    val pw = MutableLiveData("")

    private var isAutoSignInTried = false

    val onSignInSuccess = EventLiveData<AutoSignIn>()
    val onSignInFail = EventLiveData<AutoSignIn>()

    suspend fun canAutoSignIn(): Boolean {
        if (isAutoSignInTried) return false
        isAutoSignInTried = true

        return authenticator.canAutoSignIn()
    }

    fun tryManualSignIn() = viewModelScope.launch {
        if (matchWithLastSignInInfo()) {
            onSignInSuccess.emit(false)
        } else {
            onSignInFail.emit(false)
        }
    }

    private suspend fun matchWithLastSignInInfo() = authenticator.signInWithId(id.value!!, pw.value!!)
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

- Dagger, Hilt

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

*AuthenticatorModule.kt*
```kotlin
@InstallIn(ApplicationComponent::class)
@Module
abstract class AuthenticatorModule {
    @Binds
    @Singleton
    @Named("SharedPreferences")
    abstract fun bindSharedPreferencesAuthenticator(authenticator: SharedPreferencesAuthenticator): Authenticator

    @Binds
    @Singleton
    @Named("EncryptedSharedPreferences")
    abstract fun bindEncryptedSharedPreferencesAuthenticator(authenticator: EncryptedSharedPreferencesAuthenticator): Authenticator

    @Binds
    @Singleton
    @Named("DataStorePreferences")
    abstract fun bindDataStorePreferencesAuthenticator(authenticator: DataStorePreferencesAuthenticator): Authenticator

    companion object{
//        const val AUTHENTICATOR_TYPE = "EncryptedSharedPreferences"
        const val AUTHENTICATOR_TYPE = "DataStorePreferences"
    }
}
```

### Authentication abstraction

- EncryptedSharedPreferences

*EncryptedSharedPreferencesAuthenticator.kt*
```kotlin
class EncryptedSharedPreferencesAuthenticator
@Inject constructor(context: Context, @Named("SharedPreferences") authenticator: Authenticator) :
    Authenticator by ((authenticator as? SharedPreferencesAuthenticator
        ?: throw RuntimeException("Fix your type casting")).apply {
        val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val encryptedSharedPreferences =
            EncryptedSharedPreferences.create("EncryptedSharedPreferences", masterKey, context, AES256_SIV, AES256_GCM)

        replaceSharedPreferences(encryptedSharedPreferences)
    })
```

*SharedPreferencesAuthenticator.kt*
```kotlin
class SharedPreferencesAuthenticator @Inject constructor(
    context: Context, private val validator: IdValidator
) : Authenticator {
    private var sharedPreferences = context.getSharedPreferences("sharedPreferences", 0)

    fun replaceSharedPreferences(sharedPreferences: SharedPreferences) {
        logE(sharedPreferences)
        this.sharedPreferences = sharedPreferences
    }

    override suspend fun canAutoSignIn() = sharedPreferences.getBoolean(AUTO_SIGNIN_KEY, false)

    override suspend fun signUpWithId(id: String, password: String) = sharedPreferences.edit(true) {
        putString(ID_KEY, id)
        putString(PW_KEY, password)
    }

    override suspend fun signInWithId(id: String, password: String) = validator.validateIdAndPwWithOthers(
        id, password, sharedPreferences.getString(ID_KEY, ""), sharedPreferences.getString(PW_KEY, "")
    ).also {
        if (it) {
            sharedPreferences.edit(true) {
                putBoolean(AUTO_SIGNIN_KEY, true)
            }
        }
    }

    override suspend fun signOut() {
        sharedPreferences.edit(true) {
            remove(AUTO_SIGNIN_KEY)
        }
    }

    companion object {
        private const val ID_KEY = "ID"
        private const val PW_KEY = "PW"
        private const val AUTO_SIGNIN_KEY = "AUTO_SIGNIN"
    }
}
```

- DataStore

*DataStorePreferencesAuthenticator.kt*
```kotlin
class DataStorePreferencesAuthenticator @Inject constructor(context: Context, private val validator: IdValidator) :
    Authenticator {
    private val dataStore = context.createDataStore("DataStorePreferencesAuthenticator")

    override suspend fun canAutoSignIn() = runCatching {
        val pref = dataStore.data.first()
        pref[AUTO_SIGNIN_KEY] == true
    }.getOrDefault(false)

    override suspend fun signUpWithId(id: String, password: String) {
        logE("$id $password")
        dataStore.edit { pref ->
            pref[ID_KEY] = id
            pref[PW_KEY] = password
        }
    }

    override suspend fun signInWithId(id: String, password: String): Boolean {
        return runCatching {
            val pref = dataStore.data.first()
            validator.validateIdAndPwWithOthers(id, password, pref[ID_KEY], pref[PW_KEY]).also {
                if (it) {
                    dataStore.edit { pref ->
                        pref[AUTO_SIGNIN_KEY] = true
                    }
                }
            }
        }.getOrDefault(false)
    }

    override suspend fun signOut() {
        dataStore.edit { pref ->
            pref.remove(AUTO_SIGNIN_KEY)
        }
    }

    companion object {
        private val ID_KEY = preferencesKey<String>("id")
        private val PW_KEY = preferencesKey<String>("pw")
        private val AUTO_SIGNIN_KEY = preferencesKey<Boolean>("autoSignIn")
    }
}
```

- AndroidX Biometric

*BioAuth.kt*
```kotlin
@Singleton
class BioAuth @Inject constructor(private val context: Context) {
    private val promptInfo = BiometricPrompt.PromptInfo.Builder().apply {
        this.setTitle("Title")
        this.setDescription("Description")
        setNegativeButtonText("Cancel")
        setAllowedAuthenticators(AUTHENTICATORS)
    }.build()

    val biometricEnabled: Boolean
        get() = BiometricManager.from(context).canAuthenticate(AUTHENTICATORS) == BIOMETRIC_SUCCESS

    suspend fun authenticate(fragment: Fragment) = suspendCancellableCoroutine<Boolean> { continuation ->
        BiometricPrompt(fragment, ContextCompat.getMainExecutor(context), object : AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                continuation.resume(false)
            }

            override fun onAuthenticationSucceeded(result: AuthenticationResult) {
                continuation.resume(true)
            }

            override fun onAuthenticationFailed() {
                continuation.resume(false)
            }
        }).authenticate(promptInfo)
    }

    companion object {
        private const val AUTHENTICATORS = BIOMETRIC_WEAK
    }
}
```

- Kotlin gradle script
- Kotlin stdlib
- ConstraintLayout
- MDC

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
- ShapeableImageView, ShapeAppearanceModel

```kotlin
@BindingAdapter("app:useCircleOutlineWithRadius")
fun ShapeableImageView.useCircleOutlineWithRadius(radius: Float) {
    shapeAppearanceModel = ShapeAppearanceModel().withCornerSize(radius)
}
```

- Glide

```kotlin
@BindingAdapter("app:url", requireAll = false)
fun ImageView.loadUrlAsync(url: String?) {
    val anim = CircularProgressDrawable(context).apply {
        strokeWidth = 4f
        setColorSchemeColors(
            *listOf(
                R.color.colorPrimary, R.color.colorSecondary, R.color.colorError70
            ).map { context.getColor(it) }.toIntArray()
        )
        start()
    }

    if (url == null) {
        Glide.with(this).load(anim).into(this)
    } else {
        Glide.with(this).load(url)
            .transition(withCrossFade(DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()))
            .placeholder(anim).into(this)
    }
}
```

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
