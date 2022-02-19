package com.hermesjunior.imagesearcher.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * WebView that opens a specified URL automatically.
 */
class UrlWebView : WebView {

    private var _initUrl: String? = null

    /**
     * The URL to open.
     */
    var initUrl: String
        get() = _initUrl.orEmpty()
        set(value) {
            _initUrl = value
        }

    constructor(urlStr: String, context: Context) : super(context) {
        initUrl = urlStr
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init()
    }

    private fun init() {
        settings.javaScriptEnabled = true
        webChromeClient = WebChromeClient()

        webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }
        }

        loadUrl(initUrl)
    }
}
