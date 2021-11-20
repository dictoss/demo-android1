package jp.live.dictoss.mytabbedapp1

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import jp.live.dictoss.mytabbedapp1.databinding.FragmentDashboardDetailBinding
import jp.live.dictoss.mytabbedapp1.ui.home.HomeViewModel

class DashboardDetailFragment : Fragment() {

    companion object {
        fun newInstance() = DashboardDetailFragment()
    }

    private lateinit var viewModel: DashboardDetailViewModel
    private var _binding: FragmentDashboardDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var c_id: Int? = -1
    private var title: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This callback will only be called when MyFragment is at least Started.
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            // Handle the back button event
            findNavController().popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.viewModel =
            ViewModelProvider(this).get(DashboardDetailViewModel::class.java)

        this._binding = FragmentDashboardDetailBinding.inflate(inflater, container, false)

        return inflater.inflate(R.layout.fragment_dashboard_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle: Bundle? = arguments
        this.c_id = bundle?.getInt("id")
        this.title = bundle?.getString("title")

        val titleTextView: TextView? = this.view?.findViewById(R.id.fragment_detail_textView)
        titleTextView?.text = this.title
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DashboardDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}