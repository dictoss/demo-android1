package jp.live.dictoss.mytabbedapp1.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import jp.live.dictoss.mytabbedapp1.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var webView: WebView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // ref: https://memo.abridge-lab.com/?p=150
        if (this.webView == null) {
            this.webView = binding.homeWebview
        }

        if (savedInstanceState != null) {
            // 回転したときに、回転前のwebページの位置を復元します
            this.webView?.restoreState(savedInstanceState)
        }

        if (this.webView?.url == null) {
            Log.i("CONF", "webView first call")

            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.requireContext())
            val openurl : String = sharedPreferences.getString("edit_text_preference_webview_openurl", "") ?: ""
            val use_js: Boolean = sharedPreferences.getBoolean("switch_preference_webview_use_javascript", false)

            this.webView?.settings?.javaScriptEnabled = use_js
            this.webView?.clearCache(true)
            this.webView?.loadUrl(openurl)

            // If click on webview, not jump chrome.
            this.webView?.setWebViewClient(object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    return false
                }
            })
        } else {
            Log.i("CONF", "webView already call")
        }

        return root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // 回転したときに、回転前のwebページの位置を保存します
        this.webView?.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        // メモリリーク対策
        this.webView?.stopLoading()
        this.webView?.webChromeClient = null
        //this.webView?.webViewClient = null
        this.webView?.destroy()
        this.webView = null

        super.onDestroy()
    }
}