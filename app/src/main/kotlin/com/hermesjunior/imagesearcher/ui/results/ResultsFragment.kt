package com.hermesjunior.imagesearcher.ui.results

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.webkit.URLUtil
import android.webkit.WebView
import android.webkit.WebView.HitTestResult
import androidx.annotation.Keep
import androidx.fragment.app.activityViewModels
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.hermesjunior.imagesearcher.R
import com.hermesjunior.imagesearcher.ui.ChooserFragment
import com.hermesjunior.imagesearcher.ui.MainViewModel
import com.hermesjunior.imagesearcher.ui.customview.BaseFragment
import im.delight.android.webview.AdvancedWebView

@Keep
class ResultsFragment : BaseFragment() {

    companion object {
        val TAG = "ResultsFragment"
    }

    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var viewPager: ViewPager
    private lateinit var pagesAdapter: SearchPagesAdapter

    override fun onBackPressed(): Boolean {
        val webView = getCurrentWebView()
        if (webView.canGoBack()) {
            webView.goBack()
            return true
        } else {
            if (viewPager.currentItem > 0) {
                viewPager.setCurrentItem(viewPager.currentItem - 1, true)
                return true
            }
        }
        return false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setAppbarTitle("")
        viewModel.setAllowGoBack(true)
        viewModel.setShowSettingsIcon(false)
        viewModel.setShowLinkIcon(true)
        viewModel.fragmentTag = ChooserFragment.TAG

        pagesAdapter = SearchPagesAdapter(this).apply {
            setSearchResults(viewModel.getSearchResults().value!!)
        }

        viewPager = view.findViewById<ViewPager>(R.id.viewPager).apply {
            adapter = pagesAdapter
            offscreenPageLimit = pagesAdapter.count - 1
        }

        view.findViewById<TabLayout>(R.id.tabLayout).run {
            setupWithViewPager(viewPager)
            for (i in 0 until tabCount) {
                getTabAt(i)?.icon = pagesAdapter.getPageIcon(i)
            }
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val webView = getCurrentWebView()
        val result = webView.hitTestResult
        val url = result.extra
        var link = url
        var isLink = false
        val filename = URLUtil.guessFileName(url, null, null)

        if ((result.type == HitTestResult.IMAGE_TYPE ||
            result.type == HitTestResult.SRC_IMAGE_ANCHOR_TYPE) &&
            URLUtil.isNetworkUrl(url) // TODO: Cannot handle base64 blobs for now
        ) {
            Log.d(TAG, "Long clicked image $url")
            menu.add(0, 1, 0, R.string.save_image).setOnMenuItemClickListener {
                Log.d(TAG, "Downloading image $filename from $url")
                AdvancedWebView.handleDownload(context, url, filename)
                true
            }

            val message = Handler().obtainMessage()
            webView.requestFocusNodeHref(message)
            val maybeLink = message.data.getString("url")
            maybeLink?.let {
                Log.d(TAG, "Found link on image $maybeLink")
                isLink = true
                link = maybeLink
            }
        }
        if (isLink || (result.type == HitTestResult.SRC_ANCHOR_TYPE && URLUtil.isNetworkUrl(link))) {
            Log.d(TAG, "Long clicked link $link")
            menu.add(0, 2, 0, R.string.open_external_browser).setOnMenuItemClickListener {
                openBrowser(link)
                true
            }
        }
    }

    fun openBrowser() {
        openBrowser(getCurrentWebView().url)
    }

    fun openBrowser(url: String?) {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        startActivity(intent)
    }

    fun getCurrentWebView(): WebView {
        return pagesAdapter.getViewPage(viewPager.currentItem)
    }
}
