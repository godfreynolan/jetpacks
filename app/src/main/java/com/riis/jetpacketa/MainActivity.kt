package com.riis.jetpacketa

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.riis.jetpacketa.databinding.ActivityMainBinding
import com.riis.jetpacketa.features.company.CompaniesFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

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