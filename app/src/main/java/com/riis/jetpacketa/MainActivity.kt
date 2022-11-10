package com.riis.jetpacketa

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.riis.jetpacketa.features.company.CompaniesFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Show the `Companies` Fragment
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, CompaniesFragment::class.java, null, CompaniesFragment.TAG)
            .commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}