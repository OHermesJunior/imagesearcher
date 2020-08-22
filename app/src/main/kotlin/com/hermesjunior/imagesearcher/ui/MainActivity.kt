package com.hermesjunior.imagesearcher.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.appbar.AppBarLayout
import com.hermesjunior.imagesearcher.R
import com.hermesjunior.imagesearcher.ui.results.ResultsFragment
import com.hermesjunior.imagesearcher.ui.settings.SettingsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var appbarLayout: AppBarLayout
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        appbarLayout = findViewById(R.id.appbar_layout)

        viewModel.getAppbarTitle().observe(this, Observer {
            title = it
        })

        viewModel.getAllowGoBack().observe(this, Observer {
            supportActionBar?.setDisplayHomeAsUpEnabled(it)
            supportActionBar?.setDisplayShowHomeEnabled(it)
        })

        viewModel.getImageFilePath().observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(
                        R.anim.enter_from_right,
                        R.anim.exit_to_left,
                        R.anim.enter_from_left,
                        R.anim.exit_to_right
                    )
                    .replace(R.id.nav_host, EditFragment())
                    .addToBackStack(null)
                    .commit()
            }
        })

        viewModel.getSearchResults().observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(
                        R.anim.enter_from_right,
                        R.anim.exit_to_left,
                        R.anim.enter_from_left,
                        R.anim.exit_to_right
                    )
                    .replace(R.id.nav_host, ResultsFragment())
                    .addToBackStack(null)
                    .commit()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        viewModel.getShowSettingsIcon().observe(this, Observer {
            menu.findItem(R.id.action_settings).isVisible = it
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.action_settings -> {
                supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(
                        R.anim.enter_from_left,
                        R.anim.exit_to_right,
                        R.anim.enter_from_right,
                        R.anim.exit_to_left
                    )
                    .replace(R.id.nav_host, SettingsFragment())
                    .addToBackStack(null)
                    .commit()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
