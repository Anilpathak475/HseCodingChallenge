package com.anil.hse.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.anil.hse.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /*override fun onResume() {
        super.onResume()
        val navHostFragment = supportFragmentManager.fragments.first() as? NavHostFragment
        navHostFragment?.let {
            it.childFragmentManager.fragments.forEach { fragment ->
                fragment.onResume()
            }
        }
    }*/
}
