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
import java.io.File

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
                Toast.makeText(applicationContext, getString(R.string.error_image_upload), Toast.LENGTH_LONG)
                    .show()
                viewModel.shownError()
                onBackPressed()
            }
        }
        if (viewModel.getEnabledSearches("").isEmpty()) {
            // on first start no searchengine is selected so start with "Settings Dialog" as opti-in
            showSettings()
            return
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
                if (this.viewModel.getEnabledSearches("").isEmpty()) {
                    // when no searchengine is selected exit app with error message
                    Toast.makeText(this,getString(R.string.error_no_searchengine_selected), Toast.LENGTH_LONG).show()
                    finish()
                    return false
                }
                return true
            }
            R.id.action_settings -> {
                showSettings()
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

    private fun showSettings() {
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

    override fun onStop() {
        WebStorage.getInstance().deleteAllData()

        CookieManager.getInstance().removeAllCookies(null)
        CookieManager.getInstance().flush()

        val webView = WebView(this)
        webView.clearCache(true)
        webView.clearFormData()
        webView.clearHistory()
        webView.clearSslPreferences()

        try { File("$cacheDir").deleteRecursively() } catch(e: Exception) { e.printStackTrace() }
        try { File("$externalCacheDir").deleteRecursively() } catch(e: Exception) { e.printStackTrace() }
        try { File("${applicationInfo.dataDir}/WebView/").deleteRecursively() } catch(e: Exception) { e.printStackTrace() }
        try { File("${applicationInfo.dataDir}/app_webview/").deleteRecursively() } catch(e: Exception) { e.printStackTrace() }
        try { File("${filesDir}/WebView/").deleteRecursively() } catch(e: Exception) { e.printStackTrace() }
        try { File("${filesDir}/app_webview/").deleteRecursively() } catch(e: Exception) { e.printStackTrace() }
        super.onStop()
    }
}
