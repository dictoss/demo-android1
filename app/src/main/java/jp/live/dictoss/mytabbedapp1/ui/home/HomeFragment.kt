package jp.live.dictoss.mytabbedapp1.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import jp.live.dictoss.mytabbedapp1.R
import jp.live.dictoss.mytabbedapp1.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val _webView: WebView = binding.homeWebview

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.requireContext())
        val openurl : String = sharedPreferences.getString("edit_text_preference_webview_openurl", "") ?: ""
        val use_js: Boolean = sharedPreferences.getBoolean("switch_preference_webview_use_javascript", false)

        //_webView.loadUrl("http://www.pcdennokan.wjg.jp/site/top/")
        //_webView.settings.javaScriptEnabled = true
        Log.i("CONF", openurl)
        //Log.i("CONF", use_js)
        _webView.loadUrl(openurl)
        _webView.settings.javaScriptEnabled = use_js

        // If click on webview, not jump chrome.
        _webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }
        })

        homeViewModel.webview.observe(viewLifecycleOwner, Observer {
            //webView.loadUrl(it)
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}