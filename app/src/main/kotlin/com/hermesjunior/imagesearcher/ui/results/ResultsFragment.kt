package com.hermesjunior.imagesearcher.ui.results

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.fragment.app.activityViewModels
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.hermesjunior.imagesearcher.R
import com.hermesjunior.imagesearcher.ui.ChooserFragment
import com.hermesjunior.imagesearcher.ui.MainViewModel
import com.hermesjunior.imagesearcher.ui.customview.BaseFragment

@Keep
class ResultsFragment : BaseFragment() {

    companion object {
        val TAG = "ResultsFragment"
    }

    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var viewPager: ViewPager
    private lateinit var pagesAdapter: SearchPagesAdapter

    override fun onBackPressed(): Boolean {
        val webView = pagesAdapter.getViewPage(viewPager.currentItem)
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
        viewModel.setShowSettingsIcon(true)
        viewModel.fragmentTag = ChooserFragment.TAG

        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        viewPager = view.findViewById<ViewPager>(R.id.viewPager)
        pagesAdapter = SearchPagesAdapter(requireActivity())

        pagesAdapter.setSearchResults(viewModel.getSearchResults().value!!)
        viewPager.adapter = pagesAdapter
        viewPager.offscreenPageLimit = pagesAdapter.count - 1
        tabLayout.setupWithViewPager(viewPager)
        for (i in 0 until tabLayout.tabCount) {
            tabLayout.getTabAt(i)?.icon = pagesAdapter.getPageIcon(i)
        }
    }
}
