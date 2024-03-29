package com.hermesjunior.imagesearcher.ui.results

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.viewpager.widget.PagerAdapter
import com.hermesjunior.imagesearcher.model.SearchResult
import com.hermesjunior.imagesearcher.ui.customview.BaseFragment
import im.delight.android.webview.AdvancedWebView

class SearchPagesAdapter(private val context: BaseFragment) : PagerAdapter() {

    private var searchResultPages = emptyList<SearchResult>()
    private var webViewPages = emptyArray<WebView?>()

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val result = searchResultPages[position]
        Log.d("ImageSearcher", "ResultPage - ${result.engineTitle} loading: ${result.searchUrl}")
        val view = AdvancedWebView(context.activity)
        view.setThirdPartyCookiesEnabled(false)
        view.setCookiesEnabled(false)
        view.settings.databaseEnabled = false
        view.settings.domStorageEnabled = false
        view.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        view.addHttpHeader("DNT", "1") // Do not track
        view.loadUrl(result.searchUrl)
        context.registerForContextMenu(view)
        webViewPages.set(position, view)
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return "" //searchResultPages[position].engineTitle
    }

    fun getPageIcon(position: Int): Drawable {
        return searchResultPages[position].engineIcon
    }

    override fun getCount() = searchResultPages.size

    fun setSearchResults(results: List<SearchResult>) {
        searchResultPages = results
        webViewPages = arrayOfNulls(results.size)
    }

    fun getViewPage(position: Int): WebView {
        return webViewPages[position]!!
    }
}
