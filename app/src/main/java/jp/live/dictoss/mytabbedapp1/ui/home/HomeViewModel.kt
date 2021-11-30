package jp.live.dictoss.mytabbedapp1.ui.home

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _webView = MutableLiveData<WebView>()
    val webView: LiveData<WebView> = _webView
}