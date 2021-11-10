package com.fabfm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fabfm.databinding.ActivityMainBinding
import com.fabfm.ui.browse.BrowseFragment

private const val BASE_RADIOTIME_URL = "http://opml.radiotime.com"
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val baseFragment = BrowseFragment.newInstance(BASE_RADIOTIME_URL)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_main, baseFragment)
            .commit()
    }

    fun setActionBarTitle(customTitle: String) {
        supportActionBar?.title = customTitle
    }
}