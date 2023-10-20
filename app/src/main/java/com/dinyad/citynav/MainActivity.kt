package com.dinyad.citynav

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dinyad.citynav.fragments.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* injecter home fragment*/
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.home_fragment,HomeFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }
}