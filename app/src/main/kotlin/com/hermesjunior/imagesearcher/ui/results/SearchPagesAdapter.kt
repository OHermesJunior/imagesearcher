package com.hermesjunior.imagesearcher.ui.results

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.hermesjunior.imagesearcher.model.SearchResult
import im.delight.android.webview.AdvancedWebView

class SearchPagesAdapter(private val context: Context) : PagerAdapter() {

    private var searchResultPages = emptyList<SearchResult>()

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        Log.d("ImageSearcher", "ResultPage - ${searchResultPages[position].engineTitle} loading: ${searchResultPages[position].searchUrl}")
        val view = AdvancedWebView(context)
        view.setCookiesEnabled(false)
        view.loadUrl(searchResultPages[position].searchUrl)
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
    }
}
