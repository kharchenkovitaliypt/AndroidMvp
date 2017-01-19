package com.idapgroup.android.mvpsample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

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
    }
}
