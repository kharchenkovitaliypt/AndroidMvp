package com.idapgroup.android.mvpsample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.idapgroup.android.mvpsample.v2.SampleActivityV2
import com.idapgroup.android.mvpsample.v2.SampleFragmentV2
import com.idapgroup.android.mvpsample.v2.SampleLceActivityV2
import io.reactivex.plugins.RxJavaPlugins

class MainActivity : AppCompatActivity() {

    init {
        RxJavaPlugins.setErrorHandler { it.printStackTrace() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById(R.id.activity_based).setOnClickListener {
            startActivity(Intent(this, SampleActivity::class.java))
        }

        findViewById(R.id.fragment_based).setOnClickListener {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, SampleFragment())
                    .addToBackStack(null)
                    .commit()
        }

        findViewById(R.id.lce_activity_based).setOnClickListener {
            startActivity(Intent(this, SampleLceActivity::class.java))
        }

        findViewById(R.id.lce_fragment_based).setOnClickListener {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, SampleLceFragment())
                    .addToBackStack(null)
                    .commit()
        }

        findViewById(R.id.fragment_based_v2).setOnClickListener {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, SampleFragmentV2())
                    .addToBackStack(null)
                    .commit()
        }

        findViewById(R.id.activity_based_v2).setOnClickListener {
            startActivity(Intent(this, SampleActivityV2::class.java))
        }
        findViewById(R.id.lce_activity_based_v2).setOnClickListener {
            startActivity(Intent(this, SampleLceActivityV2::class.java))
        }
    }
}
