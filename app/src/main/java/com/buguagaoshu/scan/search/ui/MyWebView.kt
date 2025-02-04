package com.buguagaoshu.scan.search.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.ConsoleMessage
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MyWebView () {
    AndroidView(
        modifier = Modifier.height(225.dp),
        factory = { context ->
            WebView(context).apply {
                settings.apply {
                    javaScriptEnabled = true
                    setSupportZoom(true)
                    builtInZoomControls = true
                    displayZoomControls = true
                    useWideViewPort = true
                    loadWithOverviewMode = true
                    domStorageEnabled = true
                    databaseEnabled = true
                    setSupportMultipleWindows(true)
                    javaScriptCanOpenWindowsAutomatically = true
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
                }

                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        println("开始加载: $url")
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        println("加载完成: $url")
                    }

                    override fun onReceivedSslError(
                        view: WebView?,
                        handler: SslErrorHandler,
                        error: SslError?
                    ) {
                        handler.proceed()
                    }
                }

                webChromeClient = object : WebChromeClient() {
                    override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                        println("WebView Console: ${consoleMessage.message()}")
                        return true
                    }
                }

                loadUrl("https://metaso.cn/")
            }
        }
    )
}