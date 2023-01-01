package jp.live.dictoss.mytabbedapp1.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
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

    private var webView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_home_menu, menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        this._binding = FragmentHomeBinding.inflate(inflater, container, false)

        return this.binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                //menuInflater.inflate(R.menu.menu_example, menu)
                menuInflater.inflate(R.menu.fragment_home_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                webView = binding.homeWebview
                when (menuItem.itemId) {
                    R.id.menu_back-> {
                        Log.i("TAG", "IN onOptionsItemSelected(): back")
                        if (webView != null) {
                            webView?.goBack()
                        }
                    }
                    R.id.menu_reload-> {
                        Log.i("TAG", "IN onOptionsItemSelected(): reload")
                        if (webView != null) {
                            webView?.clearCache(true)
                            webView?.reload()
                        }
                    }
                    R.id.menu_home-> {
                        Log.i("TAG", "IN onOptionsItemSelected(): home")
                        if (webView != null) {
                            webView?.clearCache(true)

                            //val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.requireContext())
                            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
                            val openurl : String = sharedPreferences.getString("edit_text_preference_webview_openurl", "") ?: ""

                            if (openurl.isNotEmpty()) {
                                webView?.loadUrl(openurl)
                            }
                        }
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // ref: https://memo.abridge-lab.com/?p=150
        if (this.webView == null) {
            this.webView = this.binding.homeWebview
        }

        if (savedInstanceState != null) {
            // 回転したときに、回転前のwebページの位置を復元します
            this.webView?.restoreState(savedInstanceState)
        }

        if (this.webView?.url == null) {
            Log.i("CONF", "webView first call")

            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.requireContext())
            val openurl : String = sharedPreferences.getString("edit_text_preference_webview_openurl", "") ?: ""
            val useJs: Boolean = sharedPreferences.getBoolean("switch_preference_webview_use_javascript", false)

            this.webView?.settings?.javaScriptEnabled = useJs
            this.webView?.clearCache(true)
            this.webView?.loadUrl(openurl)

            // If click on WebView, not jump chrome.
            this.webView?.webViewClient = (object : WebViewClient() {
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
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // 回転したときに、回転前のwebページの位置を保存します
        this.webView?.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this._binding = null
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