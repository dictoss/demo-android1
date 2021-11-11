package jp.live.dictoss.mytabbedapp1.ui.home

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    //private val _text = MutableLiveData<String>().apply {
    //    value = "This is home Fragment"
    //}
    //val text: LiveData<String> = _text

    private val _webView = MutableLiveData<WebView>().apply {

    }
    val webview: LiveData<WebView> = _webView
}