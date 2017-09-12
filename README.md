# AndroidMvp
User case:
```kotlin
interface SampleMvp {
    interface View {
    	fun showSuccess()
	fun showError(code: Int)
    }
    inteface Presenter {
    	fun submit()
    }
}

class SamplePresenter : SampleMvp.Presenter, BasePresenter<SampleMvp.View> {

    override fun submit() {
	val request: Single<Boolean> ...
	request.disposeOnDetachView().subscribe({
	    view!!.showSuccess()
	}, {
	   if(it is ServiceError) {
	      view!!.showError(911)
	   } else {
	      view!!.showError(-1)
	   }
	}
    }
}

class SampleActivity : AppCompatActivity() {

    val presenterView =  object : SampleMvp.View {
    	override fun showSuccess() {
	    Toast.makeToast(applicationContext, "Woohooo success!!!", Toast.LONG).show()
	}
	override fun onError(code: Int) {
	   if(code == 911) {
	      Toast.makeToast(applicationContext, "We have a problem!!!", Toast.LONG).show()
	   } else {
	      throw IllegalArgumentException("Uknown error code: $code")
	   }
	}
    }
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
