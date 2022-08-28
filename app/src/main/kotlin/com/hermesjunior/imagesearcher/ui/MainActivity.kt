package com.hermesjunior.imagesearcher.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.CookieManager
import android.webkit.WebStorage
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.google.android.material.appbar.AppBarLayout
import com.hermesjunior.imagesearcher.R
import com.hermesjunior.imagesearcher.ui.customview.BaseFragment
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

        viewModel.getAppbarTitle().observe(this) {
            title = it
        }

        viewModel.getAllowGoBack().observe(this) {
            supportActionBar?.setDisplayHomeAsUpEnabled(it)
            supportActionBar?.setDisplayShowHomeEnabled(it)
        }

        viewModel.getImageFilePath().observe(this) {
            if (!it.isNullOrEmpty()) {
                supportFragmentManager.commit {
                    setCustomAnimations(
                        R.anim.enter_from_right,
                        R.anim.exit_to_left,
                        R.anim.enter_from_left,
                        R.anim.exit_to_right
                    )
                    replace(R.id.nav_host, EditFragment(), EditFragment.TAG)
                    addToBackStack(EditFragment.TAG)
                }
            }
        }

        viewModel.getSearchResults().observe(this) {
            if (!it.isNullOrEmpty()) {
                supportFragmentManager.commit {
                    setCustomAnimations(
                        R.anim.enter_from_right,
                        R.anim.exit_to_left,
                        R.anim.enter_from_left,
                        R.anim.exit_to_right
                    )
                    replace(R.id.nav_host, ResultsFragment(), ResultsFragment.TAG)
                    addToBackStack(ResultsFragment.TAG)
                }
            }
        }

        viewModel.getError().observe(this) {
            if (it) {
                Toast.makeText(applicationContext, "Failed to upload image.", Toast.LENGTH_LONG).show()
                viewModel.shownError()
                onBackPressed()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        viewModel.getShowSettingsIcon().observe(this) {
            menu.findItem(R.id.action_settings).isVisible = it
            if (it) {
                menu.findItem(R.id.action_openlink).isVisible = false
            }
        }
        viewModel.getShowLinkIcon().observe(this) {
            menu.findItem(R.id.action_openlink).isVisible = it
            if (it) {
                menu.findItem(R.id.action_settings).isVisible = false
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }
            R.id.action_settings -> {
                supportFragmentManager.commit {
                    setCustomAnimations(
                        R.anim.enter_from_left,
                        R.anim.exit_to_right,
                        R.anim.enter_from_right,
                        R.anim.exit_to_left
                    )
                    replace(R.id.nav_host, SettingsFragment())
                    addToBackStack(null)
                }
                return true
            }
            R.id.action_openlink -> {
                val fragment = supportFragmentManager.findFragmentByTag(ResultsFragment.TAG)
                (fragment as ResultsFragment).openBrowser()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        supportFragmentManager.fragments.forEach {
            if (it.isVisible) {
                if(it !is BaseFragment || !it.onBackPressed()) {
                    super.onBackPressed()
                }
            }
        }
    }

    override fun onDestroy() {
        WebStorage.getInstance().deleteAllData()

        CookieManager.getInstance().removeAllCookies(null)
        CookieManager.getInstance().flush()

        val webView = WebView(this)
        webView.clearCache(true)
        webView.clearFormData()
        webView.clearHistory()
        webView.clearSslPreferences()
        super.onDestroy()
    }
}
