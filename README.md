# AndroidMvp
User case:
```kotlin
class SampleActivity : AppCompatActivity() {

    val presenterView<SampleMvp.View> =  ...
    lateinit var presenter: SampleMvp.Presenter<SampleMvp.View>

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
	findViewById(R.id.submit).setOnClickListener {
	    presenter.submit()
	}
        ... init view ...

	val createPresenter = { SamplePresenter() }
        presenter = attachPresenter(this, presenterView, createPresenter, savedState)
	...
```

```gradle
allprojects {
    repositories {
	...
	maven { url 'https://jitpack.io' }
    }
}
dependencies {
    compile 'com.github.kharchenkovitaliypt.AndroidMvp:mvp:0.9.8'
}
```
