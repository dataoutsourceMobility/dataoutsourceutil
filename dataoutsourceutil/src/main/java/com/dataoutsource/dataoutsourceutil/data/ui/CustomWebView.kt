package com.dataoutsource.dataoutsourceutil.data.ui

import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient

open class CustomWebView(val saveCookies: (cookies:String) -> Unit) : WebViewClient() {

    override fun onPageFinished(view: WebView?, url: String?) {

        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)

        saveCookies(cookieManager.getCookie(url))
    }
}